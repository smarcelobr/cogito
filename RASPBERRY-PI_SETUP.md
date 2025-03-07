#  RASPBERRY PI 5

Passos que utilizei para transformar um RASPBERRY PI 5 em um router.

## Renomear as interfaces de rede

Objetivo: Não confundir a rede interna lan com a externa wan, renomeei o `eth0` para `lan0` e `eth1` para `wan0`.

Crie o arquivo `/etc/udev/rules.d/70-persistent-net.rules` com o conteúdo 
(tudo numa mesma linha) para mudar o nome de `eth0` para `lan0`:


    SUBSYSTEM=="net", ACTION=="add", DRIVERS=="?*", ATTR{address}=="{MAC DA INTERFACE}", ATTR{dev_id}=="0x0", ATTR{type}=="1", KERNEL=="eth*", NAME="lan0"
    SUBSYSTEM=="net", ACTION=="add", DRIVERS=="?*", ATTR{address}=="{MAC DA INTERFACE}", ATTR{dev_id}=="0x0", ATTR{type}=="1", KERNEL=="eth*", NAME="wan0"

  O endereço MAC da interface pode ser obtido no comando `ifconfig eth0` e `ifconfig eth1`.

## Habilitar o IP FORWARD no RASPBERRY PI

Edite /etc/sysctl.conf :

    # Uncomment the next line to enable packet forwarding for IPv4
    net.ipv4.ip_forward=1

Para habilitar sem reiniciar:

    $ echo 1 > /proc/sys/net/ipv4/ip_forward

## Setar o IP do raspberry no lan0 via IP estático 

Criar o arquivo ‘/etc/network/interfaces.d/00-lan0-static-ip’

    pi@rasp2:/etc/network/interfaces.d $ cat 00-lan0-static-ip
    # static IP para o lan0 (eht0) pois é um gateway
    auto lan0
    iface lan0 inet static
    address 192.168.2.1
    netmask 255.255.255.0

Neste exemplo, estou usando rede 192.168.2.0/24 para lan0

## Instalação do ‘iptables-persistent’:

Este pacote lê regras do iptables (`/etc/iptables/rules.v4`) ao iniciar o raspberry.

    $ sudo apt-get install iptables-persistent

## IPTABLES - Configurar NAT entre lan0 e wan0

### IPv4

lan0 (Ethernet) = rede “interna” do RASPBERRY PI 5 (PC OLAVO)
wlan0 (Wifi) = rede “externa” (WLAN) a ser controlada. (jardimdomeier)

    $ sudo iptables -t nat -A POSTROUTING -o wlan0 -j MASQUERADE
    $ sudo iptables -A FORWARD -i wlan0 -o lan0 -m state --state RELATED,ESTABLISHED -j ACCEPT


### Para salvar as configurações:

    $ sudo su
    $ iptables-save >/etc/iptables/rules.v4

## Instalação do DNSMASQ (DNS e DHCP server)
_ref.: https://carpie.net/articles/setting-up-a-home-network-dhcp-dns-server-with-dnsmasq_

Instalação:

    sudo apt-get install dnsmasq

Crie o novo arquivo `/etc/dnsmasq.d/home.dns`

```
# Never forward plain names (without a dot or domain part)
domain-needed

# Never forward addresses in the non-routed address spaces
bogus-priv

# interface: Atende DHCP apenas para a interface lan0 (antiga eth0)
interface=lan0
#interface=tun0

# Add local-only domains here, queries in these domains are answered
# from /etc/hosts or DHCP only.
local=/mydomain.com.br/

# dominio desta rede
domain=mydomain.com.br

dhcp-range=192.168.2.50,192.168.2.150,24h
# dhcp-option: o default já é usar as informacoes (netmask, broadcast) do próprio raspberry.
# Por isso, eu comentei a opcao abaixo.
# dhcp-option=3,192.168.2.1

## STATIC IP
dhcp-host=20:33:33:33:33:33,xeonling,192.168.2.10
dhcp-host=A4:44:44:44:44:44,00:55:55:55:55:55,olavo,192.168.2.11
dhcp-host=AC:66:66:66:66:66,EPSON9E1F18,192.168.2.12
dhcp-host=b8:77:77:77:77:77,b8:88:88:88:88:88,rasp1,192.168.2.13

# DNS
# Google public name servers: 8.8.4.4 e 8.8.8.8
# força consultar o DNS via wan0
server=8.8.4.4@wan0
server=8.8.8.8@wan0
```

Coloque os registros DNS em `/etc/hosts`:

```
127.0.0.1       localhost
::1             localhost ip6-localhost ip6-loopback
ff02::1         ip6-allnodes
ff02::2         ip6-allrouters

192.168.2.1     rasp2 rasp2.mydomain.com.br teste teste.mydomain.com.br
192.168.2.10    xeonling xeonling.mydomain.com.br
192.168.2.11    olavo olavo.mydomain.com.br
192.168.2.12    epson epson.mydomain.com.br
# 192.168.2.13    rasp1 rasp1.mydomain.com.br
```

Restarta o DNSMASQ:

    sudo systemctl restart dnsmasq

Para conferir o status:

    sudo systemctl status dnsmasq

## MARIADB

Mariadb  Ver 15.1 Distrib 10.11.6-MariaDB

### Criação do Database 

    $ mariadb --user=root --password=****

    show databases;

    CREATE DATABASE cogitoDB;
    
    CREATE OR REPLACE USER 'COGITO_OWNER'@'192.168.%' IDENTIFIED BY '****';
    GRANT ALL PRIVILEGES ON cogitoDB.* TO 'COGITO_OWNER'@'192.168.%';
    
    CREATE OR REPLACE USER 'COGITO_APP'@'192.168.%' IDENTIFIED BY '****';
    GRANT SELECT,INSERT,UPDATE,DELETE ON cogitoDB.* TO 'COGITO_APP'@'192.168.%';    

    CREATE DATABASE cogitoTestDB;
    
    CREATE OR REPLACE USER 'COGITO_TEST_OWNER'@'192.168.%' IDENTIFIED BY '*****';
    GRANT ALL PRIVILEGES ON cogitoTestDB.* TO 'COGITO_TEST_OWNER'@'192.168.%';

    CREATE OR REPLACE USER 'COGITO_TEST_APP'@'192.168.%' IDENTIFIED BY '*****';
    GRANT SELECT,INSERT,UPDATE,DELETE ON cogitoTestDB.* TO 'COGITO_TEST_APP'@'192.168.%';    


### Backup e Restore:

#### Preparação SSH

Criando par de chaves ssh para copiar arquivo de backup de source para target.

No source, caso não tenha, gerar um par de chaves:

    $ sudo ssh-keygen -t ed25519

No target, criar ou editar arquivo ~/.ssh/authorized_keys incluindo uma linha
com o conteúdo do arquivo ~/.ssh/id_ed25519.pub gerado no source.

#### Preparação MariaDB

No mariadb do target, criar um usuário 'admin_restore' para restaurar o backup:

    $ mariadb --user=root --password=****
	
	sql> CREATE OR REPLACE USER 'admin_restore'@'localhost' IDENTIFIED BY '***';
	sql> GRANT ALL PRIVILEGES ON *.* TO 'admin_restore'@'localhost';

#### FAZENDO O BACKUP
_(comandos no source)_

Gerando o backup:

    $ sudo mysqldump -u root --databases cogitoDB >2024-11-16_cogitoDB.sql

Copiando o backup para outra máquina:

    $ scp 2024-11-16_cogitoDB.sql pi@target.local:/home/pi/

#### FAZENDO O RESTORE:
_(comandos no target)_

    $ mariadb --user=admin_restore --password=*** < 2024-11-16_cogitoDB.sql

## NGINX

Para escutar a porta 80 do raspberry e realizar o proxy reverso para 
a aplicação java (spring boot JAR)

Crie arquivo de configuração do cogito no NGINX: `/etc/nginx/sites-enabled/cogito.site`

```
server {
    listen 192.168.2.1:80;
        server_name teste teste.mydomain.com.br;

    location / {
        proxy_pass http://127.0.0.1:8080;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header Host      $host;
    }

}

server {

   listen 192.168.2.1:80;
   server_name _;

   return 302 http://teste.mydomain.com.br/$request_uri;

}
```

1. A aplicação cogito lê o IP do cliente no header `X-Real-IP` que é passado
pelo NGNIX.
2. A última configuração `server` era para redirecionar qualquer chamada para o teste,
mas não funcionou.

Para checar se as configurações estão ok:

    $ sudo nginx -t

Para recarregar as novas configurações:

    $ sudo service nginx reload


## JAVA

- versão do Java: `openjdk 17.0.11 2024-04-16`


## COGITO

Aplicação JAVA que modifica iptables para liberar acesso a internet (lan0->wan0)
mediante a um resultado de teste de matemática.

Se quiser executar manualmente:

    $ cd /opt/cogito
    $ ./start.sh

Arquivo de configuração `/opt/cogito/config/application.properties`:

    # escuta o localhost na porta padrão (8080)
    # porque existe um ngnix instalado fazendo o reverse proxy
    server.address=127.0.0.1
    
    # confereConexoes.enabled: (true* ou false)
    # para habilitar/desabilitar o drop das conexões
    confereConexoes.enabled=true
    
    # descomente para DEBUG no Spring Security
    #logging.level.org.springframework.security=DEBUG
    
    # variaveis ambientais para a aplicacao COGITO.
    DB_URL=r2dbc:mariadb://192.168.2.1:3306/cogitoDB
    DB_USERNAME=COGITO_APP
    DB_PASSWORD=****
    FLYWAY_URL=jdbc:mariadb://192.168.2.1:3306/cogitoDB
    FLYWAY_USER=COGITO_OWNER
    FLYWAY_PASSWORD=****


## COGITO AS A SERVICE

### USUARIO DE SISTEMA
- user: `appcogito`
- grupo: `appjava`

ref.: https://www.baeldung.com/linux/create-non-login-user


Cria grupo de aplicações java (appjava)

    $ sudo groupadd appjava

Cria usuario de sistema appcogito (nologin e nem home)

    $ sudo adduser appcogito --system

Adiciona appcogito ao grupo appjava:

    $ sudo adduser appcogito appjava

### Usuário appcogito no sudoers

Para executar o iptables como ROOT:

Criar arquivo `/etc/sudoers.d/020_cogito`:
```
appcogito       ALL=(ALL)       NOPASSWD: ALL
```
_(O ideal seria restringir apenas para o IPTABLES mas não descobri como por causa dos parâmetros.)_

### COGITO EM /OPT

```
$ sudo mkdir /opt/cogito
$ sudo chown appcogito:appjava /opt/cogito
// copia os arquivos jar e sh
$ sudo chmod a+x /opt/cogito/start.sh
```

Script `/opt/cogito/start.sh` bash para iniciar o java:

``` 
!/bin/bash

echo INICIANDO COGITO

JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
WORKDIR=/opt/cogito
JAVA_OPTIONS=" -Xms512m -Xmx1G -server "
# APP_OPTIONS=" -c /path/to/app.config -d /path/to/datadir "
APP_OPTIONS=""

cd $WORKDIR
"${JAVA_HOME}/bin/java" $JAVA_OPTIONS -jar cogito.jar $APP_OPTIONS
```

### COGITO AS A LINUX SERVICE

Configurado no systemd para iniciar automaticamente ao iniciar o raspberry:
(ref.: https://www.baeldung.com/linux/run-java-application-as-service)
(ref.: https://www.auroria.io/spring-boot-as-systemd-service/)

Crie o arquivo `/etc/systemd/system/cogito.service`:
```
[Unit]
Description=COGITO MATH GATEWAY
After=syslog.target network.target

[Service]
SuccessExitStatus=143

User=appcogito
Group=appjava

Type=forking

ExecStart=/opt/cogito/start.sh
ExecStop=/bin/kill -15 $MAINPID

[Install]
WantedBy=multi-user.target
```

Depois que colocar o 'cogito.service' na pasta, registre-o:

    $ sudo systemctl daemon-reload

Pode startar e stopar agora:

    $ sudo systemctl start cogito.service
    $ sudo systemctl status cogito.service
    $ sudo systemctl stop cogito.service
    $ sudo systemctl status cogito.service

para ver o log:

    $ journalctl -xeu cogito.service -f


para iniciar sempre que for ligado:

    $ sudo systemctl enable cogito.service


## Referências utilizadas:

- https://kromey.us/how-to/tech/2016/01/24/raspberry-pi-as-a-gateway
- https://www.revsys.com/writings/quicktips/nat.html
- https://www.linux.com/topic/networking/iptables-rules-ipv6/


## Comandos úteis do IPTABLES

### IPv4 
Listar IPTABLES:

    $ sudo iptables -t nat -L -v -n
    $ sudo iptables -L -v -n --line-numbers

Para permitir todos os IPs de lan0:

    $ sudo iptables -A FORWARD -i lan0 -o wlan0 -j ACCEPT

Para permitir para um IP específico

    $ sudo iptables -A FORWARD -i lan0 -s 192.168.2.10 -o wlan0 -j ACCEPT

Remover permissão de um IP para a rede externa

    $ sudo iptables -I FORWARD -i lan0 -o wlan0 -s 192.168.2.10 -j DROP

Retormar permissão de um IP para a rede externa

    $ sudo iptables -D FORWARD -i lan0 -o wlan0 -s 192.168.2.10 -j DROP

ou remover pelo número da regra :

    $ sudo iptables -L -v -n --line-numbers (lista as regras)
    $ sudo iptables -D FORWARD 1  (apagar a regra 1, caso for a do IP que queira permitir)


### IPv6

Listar IPTABLES:

    $ sudo ip6tables -L -v -n

Para permitir conexões já estabelecidas:

    $ sudo ip6tables -A INPUT -m conntrack --ctstate ESTABLISHED,RELATED -j ACCEPT

Para permitir um IP específico:

    $ sudo ip6tables -A FORWARD -i lan0 -s fe80::3333:4444:5555:fe96%17 -o wlan0 -j ACCEPT

Para permitir todos os ICPMs

    $ sudo ip6tables -A INPUT -p ipv6-icmp -j ACCEPT

## Depurar com tcpdump

Exemplo filtro tcpdump para ver os pacotes de rede no Raspberry PI 5
de um determinado IP.

    $ sudo tcpdump -i wan0 src host 192.168.2.10 or dst host 192.168.2.10

## Problema no /includegraphics do LaTeX ? java.awt.HeadlessException ?

ref.: [Possible to run java UI (AWT/Swing) application on Raspberry pi 2 ...](https://raspberrypi.stackexchange.com/a/49435/77721) 

No raspberry pi, é preciso instalar/atualizar os seguinte pacotes.
(Não funcionou para resolver o problema do headless mode)

```
sudo apt-get update
sudo apt-get upgrade
sudo apt-get install openssl
sudo apt-get install xterm
sudo apt-get install xauth
sudo apt-get install libxtst6
sudo reboot
```
