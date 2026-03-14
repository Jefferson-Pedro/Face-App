package com.face_app.project.repository;

import com.face_app.project.model.FileUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IFileRepository extends JpaRepository<FileUser, UUID> {

    public Optional<FileUser> findById(UUID idArquivo);
}
