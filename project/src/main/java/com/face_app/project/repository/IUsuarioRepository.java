package com.face_app.project.repository;

import com.face_app.project.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

    public Usuario findByEmailOrCpfOrLogin (String email, String cpf, String login);
}
