# Cogito

Responda corretamente às questões para ganhar cotas de acesso à internet.

## Build Frontend First

    > cd frontend
    frontend> ng build
    frontend> cd ..
    > .\gradlew clean 

## DATABASE - MariaDB

### Criando o Database oficial e de testes

    mariadb --user=root --password=senha_do_root

    show databases;

    CREATE DATABASE cogitoDB;
    
    CREATE OR REPLACE USER 'COGITO_OWNER'@'192.168.2.%' IDENTIFIED BY '****';
    GRANT ALL PRIVILEGES ON cogitoDB.* TO 'COGITO_OWNER'@'192.168.2.%';
    
    CREATE OR REPLACE USER 'COGITO_APP'@'192.168.2.%' IDENTIFIED BY '****';
    GRANT SELECT,INSERT,UPDATE,DELETE ON cogitoDB.* TO 'COGITO_APP'@'192.168.2.%';    

    CREATE DATABASE cogitoTestDB;
    
    CREATE OR REPLACE USER 'COGITO_TEST_OWNER'@'192.168.2.%' IDENTIFIED BY 'testOwner123';
    GRANT ALL PRIVILEGES ON cogitoTestDB.* TO 'COGITO_TEST_OWNER'@'192.168.2.%';

    CREATE OR REPLACE USER 'COGITO_TEST_APP'@'192.168.2.%' IDENTIFIED BY 'test123';
    GRANT SELECT,INSERT,UPDATE,DELETE ON cogitoTestDB.* TO 'COGITO_TEST_APP'@'192.168.2.%';    

Se precisar revogar as permissões:

        REVOKE ALL PRIVILEGES ON cogitoTestDB.* FROM 'COGITO_APP'@'192.168.2.%','COGITO_OWNER'@'192.168.2.%';

