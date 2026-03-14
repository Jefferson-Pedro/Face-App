package com.face_app.project.service.face;


import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.RecognitionDTO;

public interface IFaceRecognition {

    public  byte[] toImage(String base64);
    public byte[] cropFace(byte[] imageBytes);
    public String performTraining ();
    public RecognitionDTO performRecognition (FaceDTO data);

}
