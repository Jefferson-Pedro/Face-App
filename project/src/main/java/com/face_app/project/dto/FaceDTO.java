package com.face_app.project.dto;

public class FaceDTO {

    private String type;
    private String data;

    public FaceDTO(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public FaceDTO() {}

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FaceDTO{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
