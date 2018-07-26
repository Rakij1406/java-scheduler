--
-- Table structure for table `clinicdetails`
--
DROP TABLE IF EXISTS `payment_error`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `payment_error` (
    `id` int(11) NOT NULL,
    `request_id` varchar(45) NOT NULL,
    `created_date` datetime DEFAULT NULL,
    `updated_date` datetime DEFAULT NULL,
    `version` int(11) DEFAULT '0',
    `payload` varchar(45) NOT NULL,
    `error_msg` varchar(45) NOT NULL,
    `error_code` varchar(45) NULL,
    `is_retryable` tinyint(4) DEFAULT '0' COMMENT 'true / false',
    `is_processed` tinyint(4) DEFAULT '0' COMMENT 'true / false',
    `schedular_picup_ts` datetime DEFAULT NULL,
    `service_name` varchar(45) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



CREATE TABLE `payment_status` (
    `id` int(11) NOT NULL,
    `payment_error_id` bigint(11) DEFAULT '0',
	`created_date` datetime DEFAULT NULL,
    `updated_date` datetime DEFAULT NULL,
    `error_msg` varchar(45) NOT NULL,
    `error_code` varchar(45) NULL,
    `is_retryable` tinyint(4) DEFAULT '0' COMMENT 'true / false',
    `is_processed` tinyint(4) DEFAULT '0' COMMENT 'true / false',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;




INSERT INTO `test`.`payment_error` (`id`, `request_id`, `created_date`, `updated_date`, `payload`, `version`, `error_msg`, `error_code`, `is_retryable`, `is_processed`, `schedular_picup_ts`, `service_name`) VALUES ('1', 'req_1', '2017-12-23 00:00:00', '2017-12-23 00:00:00', '1', '{{sample}}', 'test error', 'ec_1', '1', '1', '2017-12-23 00:00:00', 'test_api');

