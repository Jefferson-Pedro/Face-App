package com.face_app.project.service.face;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.model.FileUser;
import com.face_app.project.model.Users;

import java.util.UUID;

public interface IFaceService {

    public boolean registerFace(Users user, FaceDTO faceDTO);

    public boolean updateUserFace(FaceDTO faceDTO, Users user);

    public String retrainModel();
}
