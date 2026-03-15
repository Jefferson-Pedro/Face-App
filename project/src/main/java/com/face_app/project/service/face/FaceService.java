package com.face_app.project.service.face;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.model.FileUser;
import com.face_app.project.model.Users;
import com.face_app.project.repository.IFileRepository;
import com.face_app.project.service.s3.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FaceService implements IFaceService {

    @Autowired
    private IFaceRecognition faceRecognition;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private IFileRepository fileRepository;

    @Value("${io.faceapp.cropfaces}")
    private String cropFolder;

    @Override
    public boolean registerFace(Users user, FaceDTO faceDTO){
        try{

            byte[] image = faceRecognition.toImage(faceDTO.getData()); // Decodifica em base64
            byte[] cropedImage = faceRecognition.cropFace(image); // faz o corte na imagem

            if (cropedImage == null){
                throw new RuntimeException("Nenhuma face detectada na imagem");
            }

            //salvando localmente a foto
            String filePath = saveLocalFile(cropedImage, user.getIdUsuario());

            String s3Url = s3Service.uploadFile(cropedImage, user.getIdUsuario(), ".png");

            FileUser file = new FileUser(user, s3Url);
            fileRepository.save(file);
            return true;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar e registrar face: " + e.getMessage(), e);
        }
    }

    //Retreina a API
    @Override
    public String retrainModel() {
        try {
            String result = faceRecognition.performTraining();
            System.out.println("Modelo retreinado: " + result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao retreinar modelo: " + e.getMessage(), e);
        }
    }

    // Atualiza a imagfem do usuário no S3
    @Override
    public boolean updateUserFace(FaceDTO faceDTO, Users user) {
        try {
            // 1. Busca face anterior no banco
            FileUser oldFile = fileRepository.findById(user.getIdUsuario())
                    .orElse(null);

            // 2. Deleta arquivo antigo do S3 (se existir)
            if (oldFile != null) {
                s3Service.deleteFile(oldFile.getS3Url());
                fileRepository.delete(oldFile);
            }

            // 3. Remove arquivo local antigo
            cleanupLocalFile(user.getIdUsuario());

            // 4. Registra nova face
            boolean res = registerFace(user, faceDTO);

            // 5. Retreina modelo com a nova face
            retrainModel();

            return res;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar face: " + e.getMessage(), e);
        }
    }

    private void cleanupLocalFile(Integer userId){

        try {
            // Montando o caminho do arquivo
            String filename = cropFolder + File.separator + userId + "-crop.png";

            //Deletando o arquivo propriamente dito
            boolean delete = Files.deleteIfExists(Paths.get(filename));
            if (delete){
                System.out.println("Arquivo local deletado com sucesso " + filename);
            }

        }catch (IOException e){
            System.err.println("Erro: Não foi possível deletar arquivo local: " + e.getMessage());
        }

    }

    // Salvando a imagem cropada localmente para treinamento LBPH
    private String saveLocalFile (byte[] image, Integer userId) {
        try{
            String filename = cropFolder + File.separator + userId + "-crop.png";

            File file = new File(filename);
            file.getParentFile().mkdirs(); // Criando diretorio apenas se não existir;

            try(FileOutputStream fos = new FileOutputStream(file)){
                fos.write(image);
            }

            return filename;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
