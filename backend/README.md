# Cogito

Responda corretamente às questões para ganhar cotas de acesso à internet.

## Build Frontend First

    > cd frontend
    frontend> ng build
    frontend> cd ..
    > .\gradlew clean 

## DATABASE - MariaDB

### Criando o Database

    mariadb --user=root --password=senha_do_root

    show databases;

    CREATE DATABASE cogitoDB;
    
    CREATE OR REPLACE USER 'COGITO_OWNER'@'192.168.2.%' IDENTIFIED BY 'minha_super_senha';
    GRANT ALL PRIVILEGES ON cogitoDB.* TO 'COGITO_OWNER'@'192.168.2.%';
    
    CREATE OR REPLACE USER 'COGITO_APP'@'192.168.2.%' IDENTIFIED BY 'minha_super_senha';
    GRANT SELECT,INSERT,UPDATE,DELETE ON cogitoDB.* TO 'COGITO_APP'@'192.168.2.%';    
