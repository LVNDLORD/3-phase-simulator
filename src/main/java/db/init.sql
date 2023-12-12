DROP DATABASE IF EXISTS simulation_project;
CREATE DATABASE simulation_project;
USE simulation_project;

DROP USER IF EXISTS 'appuser'@'localhost';
CREATE USER 'appuser'@'localhost' IDENTIFIED BY 'password';
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP ON simulation_project.* TO 'appuser'@'localhost';