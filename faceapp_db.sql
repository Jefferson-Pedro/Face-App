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
  `idusuario` INT NOT NULL,
  `nome` VARCHAR(45) NULL,
  `telefone` VARCHAR(20) NULL,
  `login` VARCHAR(45) NULL,
  `senha` VARCHAR(255) NULL,
  `ativo` TINYINT NULL,
  `email` VARCHAR(45) NULL,
  `cpf` VARCHAR(11) NULL,
  `data_criacao` DATETIME NULL,
  PRIMARY KEY (`idusuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `faceapp`.`arquivo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `faceapp`.`arquivo` (
  `idarquivo` INT NOT NULL,
  `tipo_arquivo` VARCHAR(10) NULL,
  `usuarioid` INT NOT NULL,
  `arquivo` MEDIUMBLOB NULL,
  PRIMARY KEY (`idarquivo`),
  INDEX `fk_arquivo_usuario_idx` (`usuarioid` ASC),
  CONSTRAINT `fk_arquivo_usuario`
    FOREIGN KEY (`usuarioid`)
    REFERENCES `faceapp`.`user` (`idusuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
