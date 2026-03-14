-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema faceapp
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema faceapp
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `faceapp` DEFAULT CHARACTER SET utf8 ;
USE `faceapp` ;

-- -----------------------------------------------------
-- Table `faceapp`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `faceapp`.`user` (
  `idusuario` VARCHAR(45) NOT NULL,
  `nome` VARCHAR(45) NULL,
  `telefone` VARCHAR(20) NULL,
  `login` VARCHAR(45) NULL,
  `senha` VARCHAR(255) NULL,
  `email` VARCHAR(45) NULL,
  `cpf` VARCHAR(11) NULL,
  `data_criacao` DATETIME NULL,
  `face_cadastrada` TINYINT NOT NULL DEFAULT 0,
  `ativo` TINYINT NULL
  PRIMARY KEY (`idusuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `faceapp`.`arquivo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `faceapp`.`arquivo` (
  `idarquivo` VARCHAR(45) NOT NULL AUTO_INCREMENT,
  `usuarioid` VARCHAR(45) NOT NULL,
  `s3_url` VARCHAR(500) NULL, 
  `data_upload` DATETIME NULL,
  PRIMARY KEY (`idarquivo`),
  INDEX `fk_arquivo_usuario_idx` (`usuarioid` ASC),
  CONSTRAINT `fk_arquivo_usuario`
    FOREIGN KEY (`usuarioid`)
    REFERENCES `faceapp`.`user` (`idusuario`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION
) ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
