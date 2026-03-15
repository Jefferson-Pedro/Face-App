package com.face_app.project.service.usuario;

import com.face_app.project.dto.FaceDTO;
import com.face_app.project.dto.UserRegistrationRequest;
import com.face_app.project.dto.UserResponse;
import com.face_app.project.model.FileUser;
import com.face_app.project.model.Users;
import com.face_app.project.repository.IUsuarioRepository;
import com.face_app.project.service.face.FaceService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository userRepository;

    @Autowired
    private FaceService faceService;

    @Override
    public UserResponse registerUser(@Valid UserRegistrationRequest usuarioRequest) {

        Users user = userRepository.findByEmailOrCpfOrLogin(
                usuarioRequest.email(),
                usuarioRequest.cpf(),
                usuarioRequest.login()
        );

        if(user != null){
            throw new IllegalArgumentException("Usuário já cadastrado!");
        }

        user = usuarioRequest.toUsuario();
        userRepository.save(user);

        return UserResponse.toUserResponse(user);
    }

    @Override
    @Transactional
    public boolean registerUserFace(Integer userId, FaceDTO faceDTO) {
        // Busca usuário
        Users user = userRepository.findByIdUsuario(userId);

        if(user == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        // Verifica se já tem face cadastrada
        if (user.getFaceCadastrada()) {
            throw new IllegalArgumentException("Usuário já possui face cadastrada");
        }

        // Processa e salva face
        if(faceService.registerFace(user, faceDTO)){
            // Retreina modelo
            faceService.retrainModel();

            // Atualiza flag
            user.setFaceCadastrada(true);
            userRepository.save(user);
        }
        return true;
    }

    @Override
    public boolean update(UserRegistrationRequest usuarioRequest) {
        return false;
    }

    @Override
    public Users findById(Integer id) {
       return userRepository.findById(id).orElse(null);
    }
}
