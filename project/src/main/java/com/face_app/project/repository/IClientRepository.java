package com.face_app.project.repository;

import com.face_app.project.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClientRepository extends JpaRepository<Usuario, Integer> {

    public Usuario findByEmailOrCpfOrTelefone (String email, String cpf, String telefone);
}
