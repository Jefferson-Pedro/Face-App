package com.face_app.project.repository;

import com.face_app.project.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IUsuarioRepository extends JpaRepository<Users, Integer> {

    public Users findByEmailOrCpfOrLogin (String email, String cpf, String login);

    public Users findById(UUID id);

    public Users findByLogin (String login);
}
