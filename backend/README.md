# Cogito

Responda corretamente às questões para ganhar cotas de acesso à internet.

## Build Frontend First

    > cd frontend
    frontend> ng build
    frontend> cd ..
    > .\gradlew clean 

## Start no Raspberry

`start.sh`

    #!/bin/bash
    java -Dlogging.level.br.nom.figueiredo.sergio.cogito=TRACE -jar cogito-0.0.1-SNAPSHOT.jar

No Raspberry PI, o `\includegraphics` não funcionará porque a aplicação
roda no modo servidor e não tem display (`java.awt.headless=true`). A 
implementação do `\includegraphics` usa o java.awt.Label que exige um 
SO com display (`java.awt.headless=false`).

## Páginas:

- [Realização do Teste](http://teste/)
- [Clone de Perguntas - Local:4200](http://localhost:4200/pergunta?perguntaId=101)
- [Clone de Perguntas - PRD](http://teste/pergunta?perguntaId=102)
- [Latex](http://teste/latex)

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

