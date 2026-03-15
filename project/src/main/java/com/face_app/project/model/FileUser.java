package com.face_app.project.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "file_user")
public class FileUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idarquivo")
    private Integer idArquivo;


    @ManyToOne
    @JoinColumn(name = "idusuario", nullable = false)
    private Users usuario;

    @Column(name = "s3_url", length = 500)
    private String s3Url;


    @Column(name = "data_upload")
    private LocalDateTime dataUpload;

    public FileUser(){}

    public FileUser(Users usuario, String s3Url) {
        this.usuario = usuario;
        this.s3Url = s3Url;
    }

    public FileUser(Integer idArquivo, Users usuario, String s3Url) {
        this.idArquivo = idArquivo;
        this.usuario = usuario;
        this.s3Url = s3Url;
        this.dataUpload = LocalDateTime.now();

    }

    public Integer getIdArquivo() {
        return idArquivo;
    }

    public void setIdArquivo(Integer idArquivo) {
        this.idArquivo = idArquivo;
    }

    public Users getUsuario() {
        return usuario;
    }

    public void setUsuario(Users usuario) {
        this.usuario = usuario;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public LocalDateTime getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDateTime dataUpload) {
        this.dataUpload = dataUpload;
    }
}
