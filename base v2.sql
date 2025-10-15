CREATE DATABASE  IF NOT EXISTS `streamsutpdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE mysql;

drop database streamsutpdb

Use `streamsutpdb`;


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
  `tipoVenta` enum('COMPRAR') DEFAULT NULL,
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
INSERT INTO `detalles_orden` VALUES (3,1,18.99,18.99,'COMPRAR',3,8),(4,1,4.50,4.50,'COMPRAR',3,10),(5,1,22.99,22.99,'COMPRAR',4,11),(6,1,16.50,16.50,'COMPRAR',5,7);
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

ALTER table ordenes
drop column ciudadEnvio,
drop column codigoPostalEnvio,
drop column direccionEnvio;

--
-- Dumping data for table `ordenes`
--

LOCK TABLES `ordenes` WRITE;
/*!40000 ALTER TABLE `ordenes` DISABLE KEYS */;
INSERT INTO `ordenes` VALUES (3,'COMPLETADA','2025-07-01 19:08:33.577388',23.49,6),(4,'PROCESADA','2025-07-01 19:33:45.907545',22.99,9),(5,'COMPLETADA','2025-07-01 19:45:23.388073',16.50,9);
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
  `titulo` varchar(255) DEFAULT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `portada` VARCHAR(255) DEFAULT NULL,
  `precioComprar` decimal(38,2) DEFAULT NULL,
  `descripcion` TEXT,
  `duracion_minutos` INT,
  `genero` VARCHAR(100),
  `anio_lanzamiento` YEAR,
  `disponible` BOOLEAN DEFAULT TRUE,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;



--
-- Dumping data for table `peliculas`
--

LOCK TABLES `peliculas` WRITE;
/*!40000 ALTER TABLE `peliculas` DISABLE KEYS */;
INSERT INTO peliculas (id, titulo, imagen, portada, precioComprar, descripcion, duracion_minutos, genero, anio_lanzamiento) VALUES
(1, 'Iron Man 3', '/imagenes/cuadricula/1.jpg', '/imagenes/portadas/1.webp', 19.99, 'Con su mundo destruido, Stark busca a los responsables poniendo a prueba su temple. Acorralado, debe sobrevivir a través de su ingenio y sus instintos para proteger a sus allegados. Mientras lucha para regresar, Stark descubre la respuesta de la pregunta que lo obsesionaba: ¿El hombre hace al traje o el traje hace al hombre?', 130, 'Acción', 2013),
(2, 'Thor: Love and Thunder', '/imagenes/cuadricula/2.jpg', '/imagenes/portadas/2.webp', 17.99, 'En “Thor: Amor y trueno” de Marvel Studios, el dios del trueno une fuerzas con la rey valquiria, con Korg y con su exnovia Jane Foster, la ahora poderosa Thor, para enfrentar al asesino conocido como Gorr, el carnicero de dioses.', 119, 'Acción', 2022),
(3, 'Thor: Ragnarok', '/imagenes/cuadricula/3.jpg', '/imagenes/portadas/3.webp', 14.99, 'Thor debe detener el Ragnarok, un cataclismo que podría acabar con la civilización en Asgard. Prisionero al otro lado del universo y sin su martillo, Thor deberá impedir que Asgard caiga en manos de la malvada Hela. Primero deberá enfrentar a un Vengador en una lucha.', 130, 'Acción', 2017),
(4, 'Resident Evil: Bienvenidos a Racoon City', '/imagenes/cuadricula/4.jpg', '/imagenes/portadas/4.webp', 12.99, 'Los sobrevivientes buscan la verdad mientras luchan contra zombis en Raccoon City.', 107, 'Acción', 2021),
(5, 'Avengers: Endgame', '/imagenes/cuadricula/5.jpg', '/imagenes/portadas/5.webp', 21.99, 'En el final épico de la Saga del Infinito, los Avengers se enfrentan a Thanos. Cuando eventos devastadores arrasan con la mitad de la población mundial y fracturan sus filas, los héroes restantes luchan por avanzar. Pero deben unirse para restaurar el orden y la armonía en el universo y traer de vuelta a sus seres queridos.', 181, 'Acción', 2019),
(6, 'Wanda Vision', '/imagenes/cuadricula/6.jpg', '/imagenes/portadas/6.webp', 13.50, '“WandaVision” de Marvel Studios combina la comedia de situación clásica con el universo cinematográfico de Marvel para continuar la historia de Wanda Maximoff (Elizabeth Olsen) y Visión (Paul Bettany), una pareja de superhéroes con una vida aparentemente ideal que empieza a sospechar de la realidad. Dirigida por Matt Shakman, escrita por Jac Schaeffer.', 120, 'Acción', 2021),
(7, 'Batman', '/imagenes/cuadricula/7.jpg', '/imagenes/portadas/7.webp', 16.50, 'Matt Reeves dirige a Robert Pattinson en el rol de Batman, el detective justiciero de Ciudad Gótica, quien debe ir en una carrera contra el tiempo para detener el plan de un retorcido asesino en serie conocido como El Acertijo.', 176, 'Acción', 2022),
(8, 'StarsWars: Rogue One', '/imagenes/cuadricula/8.jpg', '/imagenes/portadas/8.webp', 18.99, 'En una época de conflicto, un grupo de héroes improbables se une en una misión para robar los planos de la Estrella de la Muerte, la última arma de destrucción del Imperio.', 133, 'Acción', 2016),
(9, 'Venom 1', '/imagenes/cuadricula/9.jpg', '/imagenes/portadas/9.webp', 11.99, 'La historia de la evolución del personaje más enigmático, complejo y rudo de Marvel: ¡Venom! Eddie Brock (Tom Hardy) es un hombre arruinado después de haber perdido todo, incluido su trabajo y su prometida.', 112, 'Acción', 2018),
(10, 'Venom 2', '/imagenes/cuadricula/10.jpg', '/imagenes/portadas/10.webp', 13.50, 'Eddie Brock intenta adaptarse a la vida compartida con su huésped alienígena, mientras busca rehacer su carrera. Pero todo cambia cuando surge un nuevo enemigo: Cletus Kasady, un asesino serial que se fusiona con un simbionte aún más letal, Carnage, desatando el caos y una batalla brutal entre ambos.', 97, 'Acción', 2021),
(11, 'Spider-Man: No Way Home', '/imagenes/cuadricula/11.jpg', '/imagenes/portadas/11.webp', 22.99, 'Por primera vez en la historia cinematográfica de Spider-Man, se revela la identidad de nuestro héroe y amigable vecino, lo cual hace que sus responsabilidades como superhéroe entren en conflicto con su vida normal, así como que corran riesgo las personas que más quiere. Cuando le pide ayuda al Doctor Strange para restituir su secreto, el hechizo provoca un hueco en su mundo.', 148, 'Acción', 2021),
(12, 'Avengers: Infinity War', '/imagenes/cuadricula/12.jpg', '/imagenes/portadas/12.webp', 20.99, 'El poderoso Thanos está a punto de hacer que la destrucción reine en el universo. Los Avengers y sus aliados superhéroes arriesgarán todo en la mejor batalla de todos los tiempos.', 149, 'Acción', 2018);
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

ALTER table usuarios
drop column plan;


--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (6,'Alejo','osmar123@gmail.com','Osmar','$2a$10$QCsV4Ef3U9d7065RoWLNp.KVTbtmYuP4fQpCKLuXjd9nPas5q8EVS','osmar123','USER'),(8,'user','user123@gmail.com','borrar','$2a$10$FvfRnnpeWVPl8lbib.nVj.zoqrJ74WVqD1z0T.Wp.nA2r3CBvNa5W','user123','USER'),(9,'General','admin@streamsutp.com','Administrador','$2a$10$GybKefY6H5GxC0AsCPNds.ihcn.PW5EISxtq7TkgVHkvah47t3AJq','adminLOL','ADMIN');
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




-- Tabla subscriptions

DROP TABLE IF EXISTS `subscripciones`;
CREATE TABLE IF NOT EXISTS `subscripciones` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `usuario_id` BIGINT NOT NULL,
  `precio_pagado` DECIMAL(10,2) NOT NULL CHECK (precio_pagado >= 0),
  `fecha_inicio` DATETIME(6) NOT NULL,
  `fecha_fin` DATETIME(6) NOT NULL,
  `status` ENUM('ACTIVE','EXPIRED','CANCELED') NOT NULL DEFAULT 'ACTIVE',
  `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
  `updated_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  KEY `idx_usuario` (`usuario_id`),
  KEY `idx_status` (`status`),
  KEY `idx_fechas` (`fecha_inicio`,`fecha_fin`),
  CONSTRAINT `fk_sub_usuario`
    FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- Trigger: validar fecha_inicio < fecha_fin y precio no negativo (INSERT)
DELIMITER $$
CREATE TRIGGER `trg_subscripciones_before_insert`
BEFORE INSERT ON `subscripciones`
FOR EACH ROW
BEGIN
  -- fecha_fin debe ser posterior a fecha_inicio
  IF NOT (NEW.fecha_fin > NEW.fecha_inicio) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'fecha_fin must be greater than fecha_inicio';
  END IF;

  -- precio_pagado >= 0 (redundante si tu MySQL aplica CHECK, pero por seguridad)
  IF NEW.precio_pagado < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'precio_pagado must be >= 0';
  END IF;

  -- impedir más de una suscripción ACTIVE por usuario
  IF NEW.status = 'ACTIVE' THEN
    IF (SELECT COUNT(*) FROM subscripciones s WHERE s.usuario_id = NEW.usuario_id AND s.status = 'ACTIVE') > 0 THEN
      SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario ya tiene una suscripción ACTIVE';
    END IF;
  END IF;
END$$
DELIMITER ;



-- Trigger: validar fecha_inicio < fecha_fin y único ACTIVE (UPDATE)
DELIMITER $$
CREATE TRIGGER `trg_subscripciones_before_update`
BEFORE UPDATE ON `subscripciones`
FOR EACH ROW
BEGIN
  -- fecha_fin debe ser posterior a fecha_inicio
  IF NOT (NEW.fecha_fin > NEW.fecha_inicio) THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'fecha_fin must be greater than fecha_inicio';
  END IF;

  -- precio_pagado >= 0
  IF NEW.precio_pagado < 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'precio_pagado must be >= 0';
  END IF;

  -- Si se está activando (NEW.status = 'ACTIVE') y antes no estaba active,
  -- verificar que no exista otra ACTIVE distinta a esta fila
  IF NEW.status = 'ACTIVE' THEN
    IF NOT (OLD.status = 'ACTIVE' AND OLD.id = NEW.id) THEN
      IF (SELECT COUNT(*) FROM subscripciones s WHERE s.usuario_id = NEW.usuario_id AND s.status = 'ACTIVE' AND s.id <> OLD.id) > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El usuario ya tiene otra suscripción ACTIVE';
      END IF;
    END IF;
  END IF;
END$$
DELIMITER ;



select * from usuarios
select * from peliculas

