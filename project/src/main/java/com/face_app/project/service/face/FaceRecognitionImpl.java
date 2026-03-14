package com.face_app.project.service.face;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.RecognitionDTO;
import org.apache.commons.codec.binary.Base64;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.IntBuffer;
import java.util.UUID;

import static org.bytedeco.opencv.global.opencv_imgcodecs.*;

@Service
public class FaceRecognitionImpl implements IFaceRecognition {

    @Value("${io.faceapp.tempfolder}")
    private String tmpFolder;

    @Value("${io.faceapp.cropfaces}")
    private String cropFolder;

    @Value("${io.faceapp.recognitionfaces}")
    private String recognitionFolder;

    @Value("${io.faceapp.haarcascade}")
    private String haarcascade;

    // Converte uma string em Base64.
    @Override
    public byte[] toImage(String base64) {

        try {
            // Remove o prefixo "data:image/png;base64," se existir
            String base64Data = base64.contains(",")
                    ? base64.split(",")[1]
                    : base64;

            return Base64.decodeBase64(base64Data);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter dados para base64: " + e.getMessage(), e);
        }
    }

    // Detecta faces em uma imagem e salva o recorte da face.
    @Override
    public byte[] cropFace (byte[] imageBytes) {

        try{
            CascadeClassifier classifier = new CascadeClassifier();
            String cascadePath = resolvePath();

            if(!classifier.load(cascadePath)){
                throw new RuntimeException("Não foi possível carregar o cascade em: " + haarcascade);
            }

            // Onde se lê a imagem
            Mat image = imdecode(
                    new Mat(imageBytes),
                    IMREAD_COLOR
            );

            if(image == null || image.empty()) {
                throw new RuntimeException("Imagem inválida ou vazia");
            }

            // Detecta faces
            RectVector faces = new RectVector();
            classifier.detectMultiScale(image, faces);

            System.out.println("Faces detectadas = " + faces.size());

            if(faces.size() == 0){
                return null; // Nesse caso, nenhuma face detectada
            }

            //Recorta a face detectada
            Mat croppedFace = new Mat(image, faces.get(0));
            BytePointer buffer = new BytePointer();
            opencv_imgcodecs.imencode(".png", croppedFace, buffer);

            // Converte BytePointer para byte[]
            byte[] result = new byte[(int) buffer.limit()];
            buffer.get(result);

            return result;
            

        }catch (Exception ex){
            throw new RuntimeException("Erro ao detectar e recortar face: " + ex.getMessage(), ex);
        }
    }

    // Método para treinamento da API
    @Override
    public String performTraining() {
        try {
            System.out.println("Starting training...");
            FaceRecognizer recognizer = LBPHFaceRecognizer.create();

            // Pegando os arquivos da pasta de faces recortadas
            File dir = new File(cropFolder);
            File[] images = dir.listFiles();

            if (images == null || images.length == 0) {
                throw new RuntimeException("Nenhuma imagem encontrada em " + cropFolder);
            }

            // Matriz de fotos
            MatVector photos = new MatVector(images.length);

            // Labels como Mat com CV_32S
            Mat labels = new Mat(images.length, 1, opencv_core.CV_32S);

            // CORREÇÃO: usa getIntBuffer() em vez de createBuffer()
            IntBuffer bufferLabels = labels.getIntBuffer();

            int contador = 0;
            int personId = 0;

            for (File currentImage : images) {
                Mat photo = opencv_imgcodecs.imread(
                        cropFolder + File.separator + currentImage.getName(),
                        opencv_imgcodecs.IMREAD_GRAYSCALE
                );

                if (photo == null || photo.empty()) {
                    System.out.println("Imagem inválida: " + currentImage.getName());
                    continue;
                }

                // Normaliza tamanho
                opencv_imgproc.resize(photo, photo, new Size(160, 160));

                photos.put(contador, photo);
                bufferLabels.put(contador, personId); // agora funciona!

                contador++;
                personId++;
            }

            // Ajusta tamanho se pulou imagens inválidas
            if (contador < images.length) {
                photos = photos.position(0).limit(contador);
                labels = labels.rowRange(0, contador);
            }

            recognizer.train(photos, labels);
            recognizer.save("LBPHTraining.yml");

            return "Complete Training com " + contador + " imagens";
        } catch (Exception e) {
            throw new RuntimeException("Aconteceu um erro: ", e);
        }
    }

    // Reconhece uma face presente em uma imagem comparando com o banco treinado.
    @Override
    public RecognitionDTO performRecognition(FaceDTO data) {
        double finalConfidence = 0;

        try {
            // passo 1 - pegar a imagem vinda como Base64 e recortar a face
            byte[] imageData = Base64.decodeBase64(data.getData());
            Mat currentFace = imdecode(new Mat(imageData), IMREAD_GRAYSCALE);
            Mat detectedface = null;

            //passo 2 - fazer um crop da imagem
            CascadeClassifier classifier = new CascadeClassifier();
            String cascadePath = resolvePath();
            boolean load = classifier.load(cascadePath);
            if (!load) {
                throw new RuntimeException("Não foi possível carregar o cascade em: " + haarcascade);
            }

            RectVector faces = new RectVector();
            classifier.detectMultiScale(currentFace, faces);
            System.out.println("Face detectada: " + faces.size());
            if (faces.size() > 0){
                detectedface = new Mat(currentFace, faces.get(0));
                imwrite(recognitionFolder + File.separator + UUID.randomUUID() + ".png", detectedface);
            }

            // passo 3 - reconhecer
            FaceRecognizer recognizer = LBPHFaceRecognizer.create();
            recognizer.read("LBPHTraining.yml");
            recognizer.setThreshold(100);

            DoublePointer confidence = new DoublePointer(1);
            IntPointer labels = new IntPointer(1);

            recognizer.predict(detectedface, labels, confidence);
            finalConfidence = confidence.get(0); // * 100 / 100;

            System.out.println("Final confidence = " + finalConfidence);
            System.out.println("Confidence to face = " + confidence.get(0) + " and label = " + labels.get(0));

            if(labels.get(0) > 0){
                return new RecognitionDTO(finalConfidence, labels.get(0));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private String resolvePath() {
        try{
           Resource resource = new ClassPathResource(haarcascade);
           File file = resource.getFile();

           return file.getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível resolver o caminho do cascade: " + haarcascade, e);
        }
    }
}
