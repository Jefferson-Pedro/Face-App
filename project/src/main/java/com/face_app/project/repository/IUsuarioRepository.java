package com.face_app.project.repository;

import com.face_app.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<User, Integer> {

    public User findByEmailOrCpfOrLogin (String email, String cpf, String login);

    public User findByLogin (String login);
}
