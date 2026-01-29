package com.face_app.project.service.facerecognition;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.RecognitionDTO;
import com.face_app.project.util.FaceMode;
import org.apache.commons.codec.binary.Base64;
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
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

    // Converte uma string em Base64 para um arquivo de imagem.
    @Override
    public String toImage(String base64Content) {

       try {
            String filename = tmpFolder + File.separator + UUID.randomUUID().toString() + ".png";

           // Remove o prefixo "data:image/png;base64," se existir
            String base64Data = base64Content;
            if(base64Content.contains(",")){
                base64Data = base64Content.split(",")[1];
            }
            // Decodificando o conteúdo base64
            byte[] imageData = Base64.decodeBase64(base64Data);
            File newImage = new File(filename);
            OutputStream output = new BufferedOutputStream(new FileOutputStream(newImage));
            output.write(imageData);
            output.close();
            return filename;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Detecta faces em uma imagem e salva o recorte da face.
    @Override
    public boolean cropFace(String filename, String extension, int mode) {

        try{
            CascadeClassifier classifier = new CascadeClassifier();
            classifier.load("haarcascade_frontalface_default.xml");

            // Onde se lê a imagem
            Mat image = imread(filename, IMREAD_COLOR);
            if(image != null){
                RectVector faces = new RectVector();
                classifier.detectMultiScale(image, faces);
                System.out.println("Faces detectadas = " + faces.size());
                if(faces.size() > 0){
                    Mat croppedFace = new Mat(image, faces.get(0));
                    String newFile = filename.replace(tmpFolder, (mode == FaceMode.REGISTER) ? cropFolder:recognitionFolder);
                    imwrite(newFile + extension, croppedFace);
                    return true;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    // Método para treinamento da API
    @Override
    public String performTraining() {
        try {
            FaceRecognizer recognizer = LBPHFaceRecognizer.create();
            // Pegando os meus arquivos e listando-os
            File dir = new File(cropFolder);
            File[] images = dir.listFiles();

            //DEBUG
            for (File f : images) {
                System.out.println("Face found: " + f.getName());
            }
            MatVector photos = new MatVector(images.length); // armazenando as fotos como matrizes
            Mat labels = new Mat(images.length, 1, opencv_core.CV_32SC1); //armazendo os identificadores para as fotos
            int contador = 0;
            IntBuffer bufferLabels = labels.createBuffer();
            int personId = 0;

            for (File currentImage : images) {
                Mat photo = opencv_imgcodecs.imread(cropFolder + File.separator + currentImage.getName(), opencv_imgcodecs.IMREAD_GRAYSCALE);
                bufferLabels.put(contador, personId);
                opencv_imgproc.resize(photo, photo, new Size(160, 160));
                photos.put(contador, photo);
                contador++;
                personId++; //Id da todo de acordo com o Id do usuário;
            }

            File f = new File("LBPHTraining.yml");

            recognizer.train(photos, labels);
            recognizer.save("LBPHTraining.yml");
            return "Complete Training";

        } catch (Exception e) {
            throw new RuntimeException("Aconteceu um erro: " , e);
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

            System.out.println("Debug - Face recebida!");

            //passo 2 - fazer um crop da imagem
            CascadeClassifier classifier = new CascadeClassifier();
            classifier.load("haarcascade_frontalface_default.xml");
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
            finalConfidence = confidence.get(0) * 100 / 100;
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
}
