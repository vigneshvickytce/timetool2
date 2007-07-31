DROP TABLE IF EXISTS task_descriptions; 
DROP TABLE IF EXISTS tasks; 

CREATE TABLE `tasks` (
  `task_id` int(11) NOT NULL,
  `task_name` varchar(40) NOT NULL,
  PRIMARY KEY  (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `task_descriptions` (
  `description_id` int(11) NOT NULL,
  `task_id` int(11) NOT NULL ,
  `description_text` varchar(40) NOT NULL ,
  PRIMARY KEY  (`description_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `task_descriptions`
  ADD CONSTRAINT `FK1853CDA8B8114` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`task_id`);

INSERT INTO `tasks` VALUES (1, 'C-ANDE02-BAYP-01');
INSERT INTO `task_descriptions` VALUES (1, 1, 'CPTK-CONFIG');
INSERT INTO `task_descriptions` VALUES (2, 1, 'CPTK-MANAGE');

INSERT INTO `tasks` VALUES (2, 'C-DSCL01-CORP-01');
INSERT INTO `task_descriptions` VALUES (3, 2, 'CPTK-MANAGE');
INSERT INTO `task_descriptions` VALUES (4, 2, 'CPTK-IENG');

INSERT INTO `tasks` VALUES (3, 'C-EDWA05-NRIV-NR');
INSERT INTO `task_descriptions` VALUES (5, 3, 'LABR-CONSULT');
INSERT INTO `task_descriptions` VALUES (6, 3, 'LABR-TRAVEL');

INSERT INTO `tasks` VALUES (4, 'C-KOHL01-MENO-WI');
INSERT INTO `task_descriptions` VALUES (7, 4, 'IMPL-CTMG');
 
INSERT INTO `tasks` VALUES (5, 'C-LIBT01-PORT-FL');
INSERT INTO `task_descriptions` VALUES (8, 5, 'CPTK-MANAGE');
INSERT INTO `task_descriptions` VALUES (9, 5, 'CPTK-DEFINE');
INSERT INTO `task_descriptions` VALUES (10, 5, 'CPTK-INTR');
INSERT INTO `task_descriptions` VALUES (11, 5, 'CPTK-TRAVEL'); 
 
INSERT INTO `tasks` VALUES (6, 'C-SUPR03-EDEN-ED');
INSERT INTO `task_descriptions` VALUES (12, 6, 'IMPL-CTMG-INTF'); 
 

INSERT INTO `tasks` VALUES (7, 'D-211'); 
INSERT INTO `task_descriptions` VALUES (13, 7, 'DADM'); 
INSERT INTO `task_descriptions` VALUES (14, 7, 'EDUC');  

INSERT INTO `tasks` VALUES (8, 'Z-PRESALE-PM'); 
INSERT INTO `task_descriptions` VALUES (15, 8, 'DEMO');  
