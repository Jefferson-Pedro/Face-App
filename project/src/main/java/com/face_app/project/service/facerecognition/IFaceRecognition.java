package com.face_app.project.service.facerecognition;


import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.RecognitionDTO;

public interface IFaceRecognition {

    public String toImage(String base64Content);
    public boolean cropFace(String filename, String extension, int model);
    public String performTraining ();
    public RecognitionDTO performRecognition (FaceDTO data);
}
