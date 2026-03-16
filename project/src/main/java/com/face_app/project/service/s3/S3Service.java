package com.face_app.project.service.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.UUID;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    /**
     * Faz upload de um arquivo para o S3
     * @param fileBytes - bytes do arquivo
     * @param userId - ID do usuário (para organização)
     * @param fileExtension - extensão do arquivo (ex: .png)
     * @return URL do arquivo no S3
     */
    public String uploadFile(byte[] fileBytes, Integer userId, String fileExtension) {
        try {
            // Gera um nome único para o arquivo
            String fileName = "faces/" + userId + "/" + System.currentTimeMillis() + ".png";

            System.out.println("Nome do arquivo: " + fileName);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType("image/png")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

            System.out.println("Link retornado do S3: "+ "https://%s.s3.%s.amazonaws.com/%s " + bucketName + region + fileName);

            // Retorna a URL pública do arquivo
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload para S3: " + e.getMessage(), e);
        }
    }

    /**
     * Deleta um arquivo do S3
     * @param fileUrl - URL completa do arquivo
     */
    public void deleteFile(String fileUrl) {
        try {
            // Extrai o key (nome do arquivo) da URL
            String key = fileUrl.substring(fileUrl.indexOf("faces/"));

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar arquivo do S3: " + e.getMessage(), e);
        }
    }
}
