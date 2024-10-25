create table IF NOT EXISTS `cota`
(
    id       INT auto_increment comment 'identifica a cota'
        primary key,
    ip       varchar(15) not null comment 'IP do computador que ganhou a cota',
    end_time datetime    not null comment 'Tempo que acaba o acesso a internet.'
) ENGINE=InnoDB DEFAULT CHARSET=UTF8
    comment 'Cotas de internet' auto_increment = 100;

