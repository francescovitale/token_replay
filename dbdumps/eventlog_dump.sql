CREATE DATABASE  IF NOT EXISTS `eventlog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `eventlog`;
-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: eventlog
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `Name` varchar(250) NOT NULL,
  `ProcessModel` varchar(45) NOT NULL,
  `Resource` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Name`),
  KEY `ProcessModel_idx` (`ProcessModel`),
  CONSTRAINT `ProcessModelAct` FOREIGN KEY (`ProcessModel`) REFERENCES `process` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES ('A','sample_pn',NULL),('B','sample_pn',NULL),('C','sample_pn',NULL),('D','sample_pn',NULL),('E','sample_pn',NULL),('F','sample_pn',NULL),('G','sample_pn',NULL),('H','sample_pn',NULL),('I','sample_pn',NULL),('L','sample_pn',NULL),('som_awaitack_dmi_1','StartOfMission','dmi'),('som_awaitack_dmi_1_merge_som_chmod_evc_2','StartOfMission',NULL),('som_awaitack_dmi_1_som_awaitack_dmi_2_split_som_chmod_evc_2','StartOfMission',NULL),('som_awaitack_dmi_2','StartOfMission','dmi'),('som_awaitack_dmi_2_merge_som_chmod_evc_2','StartOfMission',NULL),('som_checklev_evc_1','StartOfMission','evc'),('som_checklev_evc_1_merge_som_grantUN_evc_1_som_grantSR_evc_1_som_grantSN_evc_1','StartOfMission',NULL),('som_checklev_evc_1_split_som_selstart_dmi_1','StartOfMission',NULL),('som_checkpos_rbc_1','StartOfMission','rbc'),('som_checkpos_rbc_1_merge_som_storepos_rbc_1_som_checktrain_rbc_1','StartOfMission',NULL),('som_checkpos_rbc_1_split_som_openconn_rtm_1','StartOfMission',NULL),('som_checkrbcsess_rtm_1','StartOfMission','rtm'),('som_checktrain_rbc_1','StartOfMission','rbc'),('som_checktrain_rbc_1_merge_som_storevalacc_rbc_1','StartOfMission',NULL),('som_checktrain_rbc_1_split_som_checkpos_rbc_1','StartOfMission',NULL),('som_checktrainroute_rbc_1','StartOfMission','rbc'),('som_checkval_rbc_1','StartOfMission','rbc'),('som_checkval_rbc_1_merge_som_grantFS_rbc_1_som_grantOS_rbc_1_som_grantSR_rbc_1','StartOfMission',NULL),('som_chmod_evc_1','StartOfMission','evc'),('som_chmod_evc_1_merge_som_end','StartOfMission',NULL),('som_chmod_evc_2','StartOfMission','evc'),('som_chmod_evc_2_merge_som_end','StartOfMission',NULL),('som_chmod_evc_2_som_chmod_evc_1_split_som_end','StartOfMission',NULL),('som_driversel_dmi_1','StartOfMission','dmi'),('som_driversel_dmi_1_merge_som_inserttraindata_dmi_1_som_NLSHproc_evc_1','StartOfMission',NULL),('som_end','StartOfMission',NULL),('som_enterid_dmi_1','StartOfMission','dmi'),('som_enterid_dmi_1_merge_som_retry_dmi_1_som_storeid_evc_1','StartOfMission',NULL),('som_giveup_evc_1','StartOfMission','evc'),('som_giveup_evc_1_merge_som_driversel_dmi_1','StartOfMission',NULL),('som_giveup_evc_1_split_som_openconn_rtm_1','StartOfMission',NULL),('som_grantFS_rbc_1','StartOfMission','rbc'),('som_grantFS_rbc_1_merge_som_awaitack_dmi_1','StartOfMission',NULL),('som_grantFS_rbc_1_som_grantOS_rbc_1_som_grantSR_rbc_1_split_som_awaitack_dmi_1','StartOfMission',NULL),('som_grantFS_rbc_1_split_som_checkval_rbc_1','StartOfMission',NULL),('som_grantOS_rbc_1','StartOfMission','rbc'),('som_grantOS_rbc_1_merge_som_awaitack_dmi_1','StartOfMission',NULL),('som_grantOS_rbc_1_split_som_checkval_rbc_1','StartOfMission',NULL),('som_grantSN_evc_1','StartOfMission','evc'),('som_grantSN_evc_1_merge_som_awaitack_dmi_1','StartOfMission',NULL),('som_grantSN_evc_1_merge_som_awaitack_dmi_2','StartOfMission',NULL),('som_grantSN_evc_1_split_som_checklev_evc_1','StartOfMission',NULL),('som_grantSR_evc_1','StartOfMission','evc'),('som_grantSR_evc_1_merge_som_awaitack_dmi_2','StartOfMission',NULL),('som_grantSR_evc_1_split_som_checklev_evc_1','StartOfMission',NULL),('som_grantSR_rbc_1','StartOfMission','rbc'),('som_grantSR_rbc_1_merge_som_awaitack_dmi_1','StartOfMission',NULL),('som_grantSR_rbc_1_merge_som_awaitack_dmi_2','StartOfMission',NULL),('som_grantSR_rbc_1_split_som_checkval_rbc_1','StartOfMission',NULL),('som_grantUN_evc_1','StartOfMission','evc'),('som_grantUN_evc_1_merge_som_awaitack_dmi_2','StartOfMission',NULL),('som_grantUN_evc_1_som_grantSR_evc_1_som_grantSN_evc_1_split_som_awaitack_dmi_2','StartOfMission',NULL),('som_grantUN_evc_1_split_som_checklev_evc_1','StartOfMission',NULL),('som_inserttraindata_dmi_1','StartOfMission','dmi'),('som_inserttraindata_dmi_1_split_som_driversel_dmi_1','StartOfMission',NULL),('som_NLSHproc_evc_1','StartOfMission','evc'),('som_NLSHproc_evc_1_split_som_driversel_dmi_1','StartOfMission',NULL),('som_openconn_rtm_1','StartOfMission','rtm'),('som_openconn_rtm_1_merge_som_retry_dmi_2_som_checkpos_rbc_1_som_giveup_evc_1','StartOfMission',NULL),('som_retry_dmi_1','StartOfMission','dmi'),('som_retry_dmi_1_merge_som_enterid_dmi_1','StartOfMission',NULL),('som_retry_dmi_1_split_som_enterid_dmi_1','StartOfMission',NULL),('som_retry_dmi_2','StartOfMission','dmi'),('som_retry_dmi_2_merge_som_openconn_rtm_1','StartOfMission',NULL),('som_retry_dmi_2_som_validate_evc_1_split_som_openconn_rtm_1','StartOfMission',NULL),('som_retry_dmi_2_split_som_openconn_rtm_1','StartOfMission',NULL),('som_selstart_dmi_1','StartOfMission','dmi'),('som_selstart_dmi_1_merge_som_sendMAreq_rtm_1_som_checklev_evc_1','StartOfMission',NULL),('som_sendMAreq_rtm_1','StartOfMission','rtm'),('som_sendMAreq_rtm_1_split_som_selstart_dmi_1','StartOfMission',NULL),('som_start','StartOfMission',NULL),('som_start_merge_som_enterid_dmi_1','StartOfMission',NULL),('som_start_som_retry_dmi_1_split_som_enterid_dmi_1','StartOfMission',NULL),('som_storeacc_evc_1','StartOfMission','evc'),('som_storeacc_evc_1_merge_som_driversel_dmi_1','StartOfMission',NULL),('som_storeacc_evc_1_som_giveup_evc_1_split_som_driversel_dmi_1','StartOfMission',NULL),('som_storeid_evc_1','StartOfMission','evc'),('som_storeid_evc_1_split_som_enterid_dmi_1','StartOfMission',NULL),('som_storepos_rbc_1','StartOfMission','rbc'),('som_storepos_rbc_1_merge_som_storevalacc_rbc_1','StartOfMission',NULL),('som_storepos_rbc_1_som_checktrain_rbc_1_split_som_storevalacc_rbc_1','StartOfMission',NULL),('som_storepos_rbc_1_split_som_checkpos_rbc_1','StartOfMission',NULL),('som_storevalacc_rbc_1','StartOfMission','rbc'),('som_validate_evc_1','StartOfMission','evc'),('som_validate_evc_1_merge_som_openconn_rtm_1','StartOfMission',NULL),('XOR_A_B','sample_pn',NULL),('XOR_A_C','sample_pn',NULL),('XOR_C_A','sample_pn',NULL),('XOR_C_E','sample_pn',NULL),('XOR_D_F','sample_pn',NULL),('XOR_E_F','sample_pn',NULL),('XOR_F_A','sample_pn',NULL),('XOR_F_G','sample_pn',NULL),('XOR_F_H','sample_pn',NULL),('XOR_G_L','sample_pn',NULL),('XOR_I_L','sample_pn',NULL);
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activityinstance`
--

DROP TABLE IF EXISTS `activityinstance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activityinstance` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `CaseID` int DEFAULT NULL,
  `Act` varchar(45) NOT NULL,
  `ATrace` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `case_fk_idx` (`CaseID`),
  KEY `act_fk_idx` (`Act`),
  KEY `at_fk_idx` (`ATrace`),
  CONSTRAINT `act_fk` FOREIGN KEY (`Act`) REFERENCES `activity` (`Name`),
  CONSTRAINT `at_fk` FOREIGN KEY (`ATrace`) REFERENCES `anomaloustrace` (`ID`),
  CONSTRAINT `case_fk` FOREIGN KEY (`CaseID`) REFERENCES `processinstance` (`CaseID`)
) ENGINE=InnoDB AUTO_INCREMENT=311160 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activityinstance`
--

LOCK TABLES `activityinstance` WRITE;
/*!40000 ALTER TABLE `activityinstance` DISABLE KEYS */;
INSERT INTO `activityinstance` VALUES (310936,710,'A',7089),(310937,710,'E',7089),(310938,710,'C',7089),(310939,710,'I',7089),(310940,710,'F',7089),(310941,710,'XOR_F_H',7089),(310942,710,'H',7089),(310943,710,'L',7089),(310944,724,'A',7090),(310945,724,'XOR_A_C',7090),(310946,724,'C',7090),(310947,724,'XOR_C_A',7090),(310948,724,'A',7090),(310949,724,'XOR_A_C',7090),(310950,724,'C',7090),(310951,724,'XOR_C_E',7090),(310952,724,'E',7090),(310953,724,'G',7090),(310954,724,'F',7090),(310955,724,'L',7090),(310956,557,'C',7085),(310957,557,'XOR_C_A',7085),(310958,557,'A',7085),(310959,557,'A',7085),(310960,557,'XOR_A_C',7085),(310961,557,'C',7085),(310962,557,'XOR_C_A',7085),(310963,557,'A',7085),(310964,557,'XOR_A_B',7085),(310965,557,'B',7085),(310966,557,'D',7085),(310967,557,'XOR_D_F',7085),(310968,557,'F',7085),(310969,557,'XOR_F_H',7085),(310970,557,'H',7085),(310971,557,'I',7085),(310972,557,'XOR_I_L',7085),(310973,557,'L',7085),(310974,85,'A',7080),(310975,85,'D',7080),(310976,85,'B',7080),(310977,85,'F',7080),(310978,85,'XOR_F_H',7080),(310979,85,'H',7080),(310980,85,'I',7080),(310981,85,'XOR_I_L',7080),(310982,85,'L',7080),(310983,83,'C',7079),(310984,83,'XOR_C_A',7079),(310985,83,'A',7079),(310986,83,'E',7079),(310987,83,'A',7079),(310988,83,'F',7079),(310989,83,'B',7079),(310990,83,'D',7079),(310991,83,'A',7079),(310992,83,'F',7079),(310993,83,'B',7079),(310994,83,'D',7079),(310995,83,'XOR_D_F',7079),(310996,83,'F',7079),(310997,83,'XOR_F_A',7079),(310998,83,'A',7079),(310999,83,'XOR_A_C',7079),(311000,83,'C',7079),(311001,83,'XOR_C_E',7079),(311002,83,'E',7079),(311003,83,'XOR_E_F',7079),(311004,83,'F',7079),(311005,83,'XOR_F_G',7079),(311006,83,'G',7079),(311007,83,'XOR_G_L',7079),(311008,83,'L',7079),(311009,749,'A',7091),(311010,749,'XOR_A_B',7091),(311011,749,'B',7091),(311012,749,'D',7091),(311013,749,'XOR_D_F',7091),(311014,749,'F',7091),(311015,749,'XOR_F_A',7091),(311016,749,'A',7091),(311017,749,'XOR_A_B',7091),(311018,749,'B',7091),(311019,749,'D',7091),(311020,749,'H',7091),(311021,749,'F',7091),(311022,749,'I',7091),(311023,749,'XOR_I_L',7091),(311024,749,'L',7091),(311025,859,'A',7092),(311026,859,'A',7092),(311027,859,'XOR_A_C',7092),(311028,859,'C',7092),(311029,859,'C',7092),(311030,859,'F',7092),(311031,859,'E',7092),(311032,859,'A',7092),(311033,859,'XOR_A_B',7092),(311034,859,'B',7092),(311035,859,'D',7092),(311036,859,'XOR_D_F',7092),(311037,859,'F',7092),(311038,859,'C',7092),(311039,859,'XOR_C_E',7092),(311040,859,'E',7092),(311041,859,'A',7092),(311042,859,'F',7092),(311043,859,'I',7092),(311044,859,'H',7092),(311045,859,'L',7092),(311046,262,'A',7082),(311047,262,'XOR_A_B',7082),(311048,262,'B',7082),(311049,262,'D',7082),(311050,262,'G',7082),(311051,262,'F',7082),(311052,262,'L',7082),(311053,194,'B',7081),(311054,194,'D',7081),(311055,194,'A',7081),(311056,194,'G',7081),(311057,194,'F',7081),(311058,194,'L',7081),(311059,345,'A',7083),(311060,345,'D',7083),(311061,345,'B',7083),(311062,345,'F',7083),(311063,345,'C',7083),(311064,345,'XOR_C_E',7083),(311065,345,'E',7083),(311066,345,'A',7083),(311067,345,'F',7083),(311068,345,'C',7083),(311069,345,'XOR_C_A',7083),(311070,345,'A',7083),(311071,345,'E',7083),(311072,345,'H',7083),(311073,345,'F',7083),(311074,345,'I',7083),(311075,345,'XOR_I_L',7083),(311076,345,'L',7083),(311077,584,'A',7086),(311078,584,'E',7086),(311079,584,'A',7086),(311080,584,'XOR_A_C',7086),(311081,584,'C',7086),(311082,584,'F',7086),(311083,584,'B',7086),(311084,584,'D',7086),(311085,584,'XOR_D_F',7086),(311086,584,'F',7086),(311087,584,'XOR_F_H',7086),(311088,584,'H',7086),(311089,584,'I',7086),(311090,584,'XOR_I_L',7086),(311091,584,'L',7086),(311092,366,'B',7084),(311093,366,'A',7084),(311094,366,'D',7084),(311095,366,'C',7084),(311096,366,'XOR_C_E',7084),(311097,366,'E',7084),(311098,366,'XOR_E_F',7084),(311099,366,'F',7084),(311100,366,'XOR_F_A',7084),(311101,366,'A',7084),(311102,366,'F',7084),(311103,366,'XOR_F_H',7084),(311104,366,'H',7084),(311105,366,'I',7084),(311106,366,'XOR_I_L',7084),(311107,366,'L',7084),(311108,79,'A',7078),(311109,79,'XOR_A_B',7078),(311110,79,'B',7078),(311111,79,'F',7078),(311112,79,'D',7078),(311113,79,'G',7078),(311114,79,'XOR_G_L',7078),(311115,79,'L',7078),(311116,18,'A',7077),(311117,18,'D',7077),(311118,18,'XOR_D_F',7077),(311119,18,'F',7077),(311120,18,'XOR_F_H',7077),(311121,18,'H',7077),(311122,18,'B',7077),(311123,18,'I',7077),(311124,18,'XOR_I_L',7077),(311125,18,'L',7077),(311126,606,'A',7088),(311127,606,'A',7088),(311128,606,'XOR_A_C',7088),(311129,606,'C',7088),(311130,606,'C',7088),(311131,606,'XOR_C_A',7088),(311132,606,'A',7088),(311133,606,'XOR_A_B',7088),(311134,606,'B',7088),(311135,606,'F',7088),(311136,606,'D',7088),(311137,606,'I',7088),(311138,606,'H',7088),(311139,606,'L',7088),(311140,596,'A',7087),(311141,596,'XOR_A_C',7087),(311142,596,'C',7087),(311143,596,'XOR_C_E',7087),(311144,596,'E',7087),(311145,596,'A',7087),(311146,596,'F',7087),(311147,596,'C',7087),(311148,596,'XOR_C_A',7087),(311149,596,'A',7087),(311150,596,'XOR_A_C',7087),(311151,596,'C',7087),(311152,596,'B',7087),(311153,596,'D',7087),(311154,596,'A',7087),(311155,596,'I',7087),(311156,596,'F',7087),(311157,596,'XOR_F_H',7087),(311158,596,'H',7087),(311159,596,'L',7087);
/*!40000 ALTER TABLE `activityinstance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `anomaloustrace`
--

DROP TABLE IF EXISTS `anomaloustrace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anomaloustrace` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `D` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `d_fk_idx` (`D`),
  CONSTRAINT `d_fk` FOREIGN KEY (`D`) REFERENCES `description` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7093 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anomaloustrace`
--

LOCK TABLES `anomaloustrace` WRITE;
/*!40000 ALTER TABLE `anomaloustrace` DISABLE KEYS */;
INSERT INTO `anomaloustrace` VALUES (7077,7076),(7078,7077),(7079,7078),(7080,7079),(7081,7080),(7082,7081),(7083,7082),(7084,7083),(7085,7084),(7086,7085),(7087,7086),(7088,7087),(7089,7088),(7090,7089),(7091,7090),(7092,7091);
/*!40000 ALTER TABLE `anomaloustrace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `description`
--

DROP TABLE IF EXISTS `description`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `description` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Fitness` float NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7092 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `description`
--

LOCK TABLES `description` WRITE;
/*!40000 ALTER TABLE `description` DISABLE KEYS */;
INSERT INTO `description` VALUES (7076,0.732292),(7077,0.732292),(7078,0.732292),(7079,0.732292),(7080,0.732292),(7081,0.732292),(7082,0.732292),(7083,0.732292),(7084,0.732292),(7085,0.732292),(7086,0.732292),(7087,0.732292),(7088,0.732292),(7089,0.732292),(7090,0.732292),(7091,0.732292);
/*!40000 ALTER TABLE `description` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `Timestamp` timestamp(3) NOT NULL,
  `Resource` varchar(45) NOT NULL,
  `ActInst` int NOT NULL,
  `CaseID` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `Case_idx` (`CaseID`),
  KEY `actinst_fk_idx` (`ActInst`),
  CONSTRAINT `actinst_fk` FOREIGN KEY (`ActInst`) REFERENCES `activityinstance` (`ID`),
  CONSTRAINT `Case` FOREIGN KEY (`CaseID`) REFERENCES `processinstance` (`CaseID`)
) ENGINE=InnoDB AUTO_INCREMENT=311158 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES (310934,'1999-12-31 23:00:00.000','',310936,710),(310935,'1999-12-31 23:00:00.001','',310937,710),(310936,'1999-12-31 23:00:00.002','',310938,710),(310937,'1999-12-31 23:00:00.003','',310939,710),(310938,'1999-12-31 23:00:00.004','',310940,710),(310939,'1999-12-31 23:00:00.005','',310941,710),(310940,'1999-12-31 23:00:00.006','',310942,710),(310941,'1999-12-31 23:00:00.007','',310943,710),(310942,'1999-12-31 23:00:00.000','',310944,724),(310943,'1999-12-31 23:00:00.001','',310945,724),(310944,'1999-12-31 23:00:00.002','',310946,724),(310945,'1999-12-31 23:00:00.003','',310947,724),(310946,'1999-12-31 23:00:00.004','',310948,724),(310947,'1999-12-31 23:00:00.005','',310949,724),(310948,'1999-12-31 23:00:00.006','',310950,724),(310949,'1999-12-31 23:00:00.007','',310951,724),(310950,'1999-12-31 23:00:00.008','',310952,724),(310951,'1999-12-31 23:00:00.009','',310953,724),(310952,'1999-12-31 23:00:00.010','',310954,724),(310953,'1999-12-31 23:00:00.011','',310955,724),(310954,'1999-12-31 23:00:00.000','',310956,557),(310955,'1999-12-31 23:00:00.001','',310957,557),(310956,'1999-12-31 23:00:00.002','',310958,557),(310957,'1999-12-31 23:00:00.003','',310959,557),(310958,'1999-12-31 23:00:00.004','',310960,557),(310959,'1999-12-31 23:00:00.005','',310961,557),(310960,'1999-12-31 23:00:00.006','',310962,557),(310961,'1999-12-31 23:00:00.007','',310963,557),(310962,'1999-12-31 23:00:00.008','',310964,557),(310963,'1999-12-31 23:00:00.009','',310965,557),(310964,'1999-12-31 23:00:00.010','',310966,557),(310965,'1999-12-31 23:00:00.011','',310967,557),(310966,'1999-12-31 23:00:00.012','',310968,557),(310967,'1999-12-31 23:00:00.013','',310969,557),(310968,'1999-12-31 23:00:00.014','',310970,557),(310969,'1999-12-31 23:00:00.015','',310971,557),(310970,'1999-12-31 23:00:00.016','',310972,557),(310971,'1999-12-31 23:00:00.017','',310973,557),(310972,'1999-12-31 23:00:00.000','',310974,85),(310973,'1999-12-31 23:00:00.001','',310975,85),(310974,'1999-12-31 23:00:00.002','',310976,85),(310975,'1999-12-31 23:00:00.003','',310977,85),(310976,'1999-12-31 23:00:00.004','',310978,85),(310977,'1999-12-31 23:00:00.005','',310979,85),(310978,'1999-12-31 23:00:00.006','',310980,85),(310979,'1999-12-31 23:00:00.007','',310981,85),(310980,'1999-12-31 23:00:00.008','',310982,85),(310981,'1999-12-31 23:00:00.000','',310983,83),(310982,'1999-12-31 23:00:00.001','',310984,83),(310983,'1999-12-31 23:00:00.002','',310985,83),(310984,'1999-12-31 23:00:00.003','',310986,83),(310985,'1999-12-31 23:00:00.004','',310987,83),(310986,'1999-12-31 23:00:00.005','',310988,83),(310987,'1999-12-31 23:00:00.006','',310989,83),(310988,'1999-12-31 23:00:00.007','',310990,83),(310989,'1999-12-31 23:00:00.008','',310991,83),(310990,'1999-12-31 23:00:00.009','',310992,83),(310991,'1999-12-31 23:00:00.010','',310993,83),(310992,'1999-12-31 23:00:00.011','',310994,83),(310993,'1999-12-31 23:00:00.012','',310995,83),(310994,'1999-12-31 23:00:00.013','',310996,83),(310995,'1999-12-31 23:00:00.014','',310997,83),(310996,'1999-12-31 23:00:00.015','',310998,83),(310997,'1999-12-31 23:00:00.016','',310999,83),(310998,'1999-12-31 23:00:00.017','',311000,83),(310999,'1999-12-31 23:00:00.018','',311001,83),(311000,'1999-12-31 23:00:00.019','',311002,83),(311001,'1999-12-31 23:00:00.020','',311003,83),(311002,'1999-12-31 23:00:00.021','',311004,83),(311003,'1999-12-31 23:00:00.022','',311005,83),(311004,'1999-12-31 23:00:00.023','',311006,83),(311005,'1999-12-31 23:00:00.024','',311007,83),(311006,'1999-12-31 23:00:00.025','',311008,83),(311007,'1999-12-31 23:00:00.000','',311009,749),(311008,'1999-12-31 23:00:00.001','',311010,749),(311009,'1999-12-31 23:00:00.002','',311011,749),(311010,'1999-12-31 23:00:00.003','',311012,749),(311011,'1999-12-31 23:00:00.004','',311013,749),(311012,'1999-12-31 23:00:00.005','',311014,749),(311013,'1999-12-31 23:00:00.006','',311015,749),(311014,'1999-12-31 23:00:00.007','',311016,749),(311015,'1999-12-31 23:00:00.008','',311017,749),(311016,'1999-12-31 23:00:00.009','',311018,749),(311017,'1999-12-31 23:00:00.010','',311019,749),(311018,'1999-12-31 23:00:00.011','',311020,749),(311019,'1999-12-31 23:00:00.012','',311021,749),(311020,'1999-12-31 23:00:00.013','',311022,749),(311021,'1999-12-31 23:00:00.014','',311023,749),(311022,'1999-12-31 23:00:00.015','',311024,749),(311023,'1999-12-31 23:00:00.000','',311025,859),(311024,'1999-12-31 23:00:00.001','',311026,859),(311025,'1999-12-31 23:00:00.002','',311027,859),(311026,'1999-12-31 23:00:00.003','',311028,859),(311027,'1999-12-31 23:00:00.004','',311029,859),(311028,'1999-12-31 23:00:00.005','',311030,859),(311029,'1999-12-31 23:00:00.006','',311031,859),(311030,'1999-12-31 23:00:00.007','',311032,859),(311031,'1999-12-31 23:00:00.008','',311033,859),(311032,'1999-12-31 23:00:00.009','',311034,859),(311033,'1999-12-31 23:00:00.010','',311035,859),(311034,'1999-12-31 23:00:00.011','',311036,859),(311035,'1999-12-31 23:00:00.012','',311037,859),(311036,'1999-12-31 23:00:00.013','',311038,859),(311037,'1999-12-31 23:00:00.014','',311039,859),(311038,'1999-12-31 23:00:00.015','',311040,859),(311039,'1999-12-31 23:00:00.016','',311041,859),(311040,'1999-12-31 23:00:00.017','',311042,859),(311041,'1999-12-31 23:00:00.018','',311043,859),(311042,'1999-12-31 23:00:00.019','',311044,859),(311043,'1999-12-31 23:00:00.020','',311045,859),(311044,'1999-12-31 23:00:00.000','',311046,262),(311045,'1999-12-31 23:00:00.001','',311047,262),(311046,'1999-12-31 23:00:00.002','',311048,262),(311047,'1999-12-31 23:00:00.003','',311049,262),(311048,'1999-12-31 23:00:00.004','',311050,262),(311049,'1999-12-31 23:00:00.005','',311051,262),(311050,'1999-12-31 23:00:00.006','',311052,262),(311051,'1999-12-31 23:00:00.000','',311053,194),(311052,'1999-12-31 23:00:00.001','',311054,194),(311053,'1999-12-31 23:00:00.002','',311055,194),(311054,'1999-12-31 23:00:00.003','',311056,194),(311055,'1999-12-31 23:00:00.004','',311057,194),(311056,'1999-12-31 23:00:00.005','',311058,194),(311057,'1999-12-31 23:00:00.000','',311059,345),(311058,'1999-12-31 23:00:00.001','',311060,345),(311059,'1999-12-31 23:00:00.002','',311061,345),(311060,'1999-12-31 23:00:00.003','',311062,345),(311061,'1999-12-31 23:00:00.004','',311063,345),(311062,'1999-12-31 23:00:00.005','',311064,345),(311063,'1999-12-31 23:00:00.006','',311065,345),(311064,'1999-12-31 23:00:00.007','',311066,345),(311065,'1999-12-31 23:00:00.008','',311067,345),(311066,'1999-12-31 23:00:00.009','',311068,345),(311067,'1999-12-31 23:00:00.010','',311069,345),(311068,'1999-12-31 23:00:00.011','',311070,345),(311069,'1999-12-31 23:00:00.012','',311071,345),(311070,'1999-12-31 23:00:00.013','',311072,345),(311071,'1999-12-31 23:00:00.014','',311073,345),(311072,'1999-12-31 23:00:00.015','',311074,345),(311073,'1999-12-31 23:00:00.016','',311075,345),(311074,'1999-12-31 23:00:00.017','',311076,345),(311075,'1999-12-31 23:00:00.000','',311077,584),(311076,'1999-12-31 23:00:00.001','',311078,584),(311077,'1999-12-31 23:00:00.002','',311079,584),(311078,'1999-12-31 23:00:00.003','',311080,584),(311079,'1999-12-31 23:00:00.004','',311081,584),(311080,'1999-12-31 23:00:00.005','',311082,584),(311081,'1999-12-31 23:00:00.006','',311083,584),(311082,'1999-12-31 23:00:00.007','',311084,584),(311083,'1999-12-31 23:00:00.008','',311085,584),(311084,'1999-12-31 23:00:00.009','',311086,584),(311085,'1999-12-31 23:00:00.010','',311087,584),(311086,'1999-12-31 23:00:00.011','',311088,584),(311087,'1999-12-31 23:00:00.012','',311089,584),(311088,'1999-12-31 23:00:00.013','',311090,584),(311089,'1999-12-31 23:00:00.014','',311091,584),(311090,'1999-12-31 23:00:00.000','',311092,366),(311091,'1999-12-31 23:00:00.001','',311093,366),(311092,'1999-12-31 23:00:00.002','',311094,366),(311093,'1999-12-31 23:00:00.003','',311095,366),(311094,'1999-12-31 23:00:00.004','',311096,366),(311095,'1999-12-31 23:00:00.005','',311097,366),(311096,'1999-12-31 23:00:00.006','',311098,366),(311097,'1999-12-31 23:00:00.007','',311099,366),(311098,'1999-12-31 23:00:00.008','',311100,366),(311099,'1999-12-31 23:00:00.009','',311101,366),(311100,'1999-12-31 23:00:00.010','',311102,366),(311101,'1999-12-31 23:00:00.011','',311103,366),(311102,'1999-12-31 23:00:00.012','',311104,366),(311103,'1999-12-31 23:00:00.013','',311105,366),(311104,'1999-12-31 23:00:00.014','',311106,366),(311105,'1999-12-31 23:00:00.015','',311107,366),(311106,'1999-12-31 23:00:00.000','',311108,79),(311107,'1999-12-31 23:00:00.001','',311109,79),(311108,'1999-12-31 23:00:00.002','',311110,79),(311109,'1999-12-31 23:00:00.003','',311111,79),(311110,'1999-12-31 23:00:00.004','',311112,79),(311111,'1999-12-31 23:00:00.005','',311113,79),(311112,'1999-12-31 23:00:00.006','',311114,79),(311113,'1999-12-31 23:00:00.007','',311115,79),(311114,'1999-12-31 23:00:00.000','',311116,18),(311115,'1999-12-31 23:00:00.001','',311117,18),(311116,'1999-12-31 23:00:00.002','',311118,18),(311117,'1999-12-31 23:00:00.003','',311119,18),(311118,'1999-12-31 23:00:00.004','',311120,18),(311119,'1999-12-31 23:00:00.005','',311121,18),(311120,'1999-12-31 23:00:00.006','',311122,18),(311121,'1999-12-31 23:00:00.007','',311123,18),(311122,'1999-12-31 23:00:00.008','',311124,18),(311123,'1999-12-31 23:00:00.009','',311125,18),(311124,'1999-12-31 23:00:00.000','',311126,606),(311125,'1999-12-31 23:00:00.001','',311127,606),(311126,'1999-12-31 23:00:00.002','',311128,606),(311127,'1999-12-31 23:00:00.003','',311129,606),(311128,'1999-12-31 23:00:00.004','',311130,606),(311129,'1999-12-31 23:00:00.005','',311131,606),(311130,'1999-12-31 23:00:00.006','',311132,606),(311131,'1999-12-31 23:00:00.007','',311133,606),(311132,'1999-12-31 23:00:00.008','',311134,606),(311133,'1999-12-31 23:00:00.009','',311135,606),(311134,'1999-12-31 23:00:00.010','',311136,606),(311135,'1999-12-31 23:00:00.011','',311137,606),(311136,'1999-12-31 23:00:00.012','',311138,606),(311137,'1999-12-31 23:00:00.013','',311139,606),(311138,'1999-12-31 23:00:00.000','',311140,596),(311139,'1999-12-31 23:00:00.001','',311141,596),(311140,'1999-12-31 23:00:00.002','',311142,596),(311141,'1999-12-31 23:00:00.003','',311143,596),(311142,'1999-12-31 23:00:00.004','',311144,596),(311143,'1999-12-31 23:00:00.005','',311145,596),(311144,'1999-12-31 23:00:00.006','',311146,596),(311145,'1999-12-31 23:00:00.007','',311147,596),(311146,'1999-12-31 23:00:00.008','',311148,596),(311147,'1999-12-31 23:00:00.009','',311149,596),(311148,'1999-12-31 23:00:00.010','',311150,596),(311149,'1999-12-31 23:00:00.011','',311151,596),(311150,'1999-12-31 23:00:00.012','',311152,596),(311151,'1999-12-31 23:00:00.013','',311153,596),(311152,'1999-12-31 23:00:00.014','',311154,596),(311153,'1999-12-31 23:00:00.015','',311155,596),(311154,'1999-12-31 23:00:00.016','',311156,596),(311155,'1999-12-31 23:00:00.017','',311157,596),(311156,'1999-12-31 23:00:00.018','',311158,596),(311157,'1999-12-31 23:00:00.019','',311159,596);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `process`
--

DROP TABLE IF EXISTS `process`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `process` (
  `Name` varchar(45) NOT NULL,
  PRIMARY KEY (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `process`
--

LOCK TABLES `process` WRITE;
/*!40000 ALTER TABLE `process` DISABLE KEYS */;
INSERT INTO `process` VALUES ('sample_pn'),('StartOfMission');
/*!40000 ALTER TABLE `process` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `processinstance`
--

DROP TABLE IF EXISTS `processinstance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `processinstance` (
  `CaseID` int NOT NULL,
  `ProcessModel` varchar(45) NOT NULL,
  PRIMARY KEY (`CaseID`),
  KEY `ProcessModel_idx` (`ProcessModel`),
  CONSTRAINT `ProcessModel` FOREIGN KEY (`ProcessModel`) REFERENCES `process` (`Name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `processinstance`
--

LOCK TABLES `processinstance` WRITE;
/*!40000 ALTER TABLE `processinstance` DISABLE KEYS */;
INSERT INTO `processinstance` VALUES (18,'sample_pn'),(79,'sample_pn'),(83,'sample_pn'),(85,'sample_pn'),(194,'sample_pn'),(262,'sample_pn'),(345,'sample_pn'),(366,'sample_pn'),(557,'sample_pn'),(584,'sample_pn'),(596,'sample_pn'),(606,'sample_pn'),(710,'sample_pn'),(724,'sample_pn'),(749,'sample_pn'),(859,'sample_pn');
/*!40000 ALTER TABLE `processinstance` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-25 21:45:53
