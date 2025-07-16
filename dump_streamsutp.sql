CREATE DATABASE  IF NOT EXISTS `streamsutpdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `streamsutpdb`;
-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: streamsutpdb
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `detalles_orden`
--

DROP TABLE IF EXISTS `detalles_orden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalles_orden` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad` int DEFAULT NULL,
  `precioUnitario` decimal(38,2) DEFAULT NULL,
  `subtotal` decimal(38,2) DEFAULT NULL,
  `tipoVenta` enum('ALQUILER','COMPRAR') DEFAULT NULL,
  `id_orden` bigint NOT NULL,
  `id_pelicula` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK93hpoipm998rdmdv4l2dyi44i` (`id_orden`),
  KEY `FK58xblmu7biu76bohjgmft93ss` (`id_pelicula`),
  CONSTRAINT `FK58xblmu7biu76bohjgmft93ss` FOREIGN KEY (`id_pelicula`) REFERENCES `peliculas` (`id`),
  CONSTRAINT `FK93hpoipm998rdmdv4l2dyi44i` FOREIGN KEY (`id_orden`) REFERENCES `ordenes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalles_orden`
--

LOCK TABLES `detalles_orden` WRITE;
/*!40000 ALTER TABLE `detalles_orden` DISABLE KEYS */;
INSERT INTO `detalles_orden` VALUES (3,1,18.99,18.99,'COMPRAR',3,8),(4,1,4.50,4.50,'ALQUILER',3,10),(5,1,22.99,22.99,'COMPRAR',4,11),(6,1,16.50,16.50,'COMPRAR',5,7);
/*!40000 ALTER TABLE `detalles_orden` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ordenes`
--

DROP TABLE IF EXISTS `ordenes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ordenes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ciudadEnvio` varchar(255) DEFAULT NULL,
  `codigoPostalEnvio` varchar(255) DEFAULT NULL,
  `direccionEnvio` varchar(255) DEFAULT NULL,
  `estado_orden` enum('CANCELADA','COMPLETADA','PENDIENTE','PROCESADA') NOT NULL,
  `fechaOrden` datetime(6) DEFAULT NULL,
  `total_orden` decimal(10,2) NOT NULL,
  `id_usuario` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKd8lswiv90edntheuacavam69a` (`id_usuario`),
  CONSTRAINT `FKd8lswiv90edntheuacavam69a` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ordenes`
--

LOCK TABLES `ordenes` WRITE;
/*!40000 ALTER TABLE `ordenes` DISABLE KEYS */;
INSERT INTO `ordenes` VALUES (3,NULL,NULL,NULL,'COMPLETADA','2025-07-01 19:08:33.577388',23.49,6),(4,NULL,NULL,NULL,'PROCESADA','2025-07-01 19:33:45.907545',22.99,9),(5,NULL,NULL,NULL,'COMPLETADA','2025-07-01 19:45:23.388073',16.50,9);
/*!40000 ALTER TABLE `ordenes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `peliculas`
--

DROP TABLE IF EXISTS `peliculas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `peliculas` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `imagen` varchar(255) DEFAULT NULL,
  `precioAlquilar` decimal(38,2) DEFAULT NULL,
  `precioComprar` decimal(38,2) DEFAULT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `peliculas`
--

LOCK TABLES `peliculas` WRITE;
/*!40000 ALTER TABLE `peliculas` DISABLE KEYS */;
INSERT INTO `peliculas` VALUES (1,'/imagenes/cuadricula/1.jpg',5.99,19.99,'IronMan 3'),(2,'/imagenes/cuadricula/2.jpg',5.99,17.99,'Thor: Love and Thunder'),(3,'/imagenes/cuadricula/3.jpg',4.99,14.99,'Thor: Ragnarok'),(4,'/imagenes/cuadricula/4.jpg',3.99,12.99,'Resident Evil: Bienvenidos a Racoon City'),(5,'/imagenes/cuadricula/5.jpg',6.99,21.99,'Avengers: Endgame'),(6,'/imagenes/cuadricula/6.jpg',4.50,13.50,'Wanda Vision'),(7,'/imagenes/cuadricula/7.jpg',5.50,16.50,'Batman'),(8,'/imagenes/cuadricula/8.jpg',5.99,18.99,'StarsWars: Rogue One'),(9,'/imagenes/cuadricula/9.jpg',3.99,11.99,'Venom 1'),(10,'/imagenes/cuadricula/10.jpg',4.50,13.50,'Venom 2'),(11,'/imagenes/cuadricula/11.jpg',6.99,22.99,'SpiderMan: No Way Home'),(12,'/imagenes/cuadricula/12.jpg',6.99,20.99,'Avengers: Infinity War');
/*!40000 ALTER TABLE `peliculas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `apellidos` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `nombres` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `plan` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `rol` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (6,'Alejo','osmar123@gmail.com','Osmar','$2a$10$QCsV4Ef3U9d7065RoWLNp.KVTbtmYuP4fQpCKLuXjd9nPas5q8EVS','estandar','osmar123','USER'),(8,'user','user123@gmail.com','borrar','$2a$10$FvfRnnpeWVPl8lbib.nVj.zoqrJ74WVqD1z0T.Wp.nA2r3CBvNa5W','estandar','user123','USER'),(9,'General','admin@streamsutp.com','Administrador','$2a$10$GybKefY6H5GxC0AsCPNds.ihcn.PW5EISxtq7TkgVHkvah47t3AJq','none','adminLOL','ADMIN');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-01 15:08:45
