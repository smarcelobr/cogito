create table IF NOT EXISTS `pergunta`
(
    id INT auto_increment comment 'identifica a pergunta'
        primary key,
    disciplina VARCHAR(40) not null,
    questao    TEXT not null comment 'Texto da pergunta em LaTeX.'
) ENGINE=InnoDB DEFAULT CHARSET=UTF8
    comment 'Perguntas de multipla escolha.' auto_increment = 100;

create table opcao
(
    id          int auto_increment comment 'identifica a opção'
        primary key,
    pergunta_id int  not null comment 'Identifica a pergunta',
    alternativa text not null comment 'Texto da opção (em LaTeX)',
    constraint opcao_pergunta_fk
        foreign key (pergunta_id) references pergunta (id)
            on delete cascade
)
    comment 'Opções das perguntas' DEFAULT CHARSET = UTF8
                                   auto_increment = 100;

create table resposta
(
    pergunta_id int not null comment 'Identifica a pergunta',
    opcao_id int not null comment 'identifica a opção escolhida',
    correta bool default false,
    explicacao text null comment 'Texto da resposta (em LaTeX)',
    constraint resposta_pk primary key (pergunta_id, opcao_id),
    constraint resposta_pergunta_fk
        foreign key (pergunta_id) references pergunta (id)
            on delete cascade,
    constraint resposta_opcao_fk
        foreign key (opcao_id) references opcao (id)
            on delete cascade
)
    comment 'Respostas corretas e incorretas.' DEFAULT CHARSET = UTF8
                                   auto_increment = 100;

