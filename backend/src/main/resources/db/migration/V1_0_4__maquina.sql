create table IF NOT EXISTS `maquina`
(
    id INT auto_increment comment 'identifica a maquina.'
        primary key,
    nome varchar(15) not null comment 'Nome da máquina'
) ENGINE=InnoDB DEFAULT CHARSET=UTF8
    comment 'Máquinas controladas' auto_increment = 100;

create table IF NOT EXISTS `maquina_ip`
(id INT auto_increment comment 'identifica o registro de IP'
     primary key,
    maquina_id INT not null comment 'identificador da maquina.',
    versao_ip char(1) default '4' not null comment 'Indica versão do IP. Pode ser "4" ou "6"',
    ip varchar(40) not null comment 'IP da máquina. ex ipv4: "192.168.2.10" / ex ipv6: "2001:0000:130F:0000:0000:09C0:876A:130B".',
    constraint maquina_ip_uk unique key (maquina_id, ip),
    constraint maquina_ip_fk
        foreign key (maquina_id) references maquina (id)
            on delete cascade
) ENGINE=InnoDB DEFAULT CHARSET=UTF8
    comment 'IPs das máquinas controladas' auto_increment = 100;


