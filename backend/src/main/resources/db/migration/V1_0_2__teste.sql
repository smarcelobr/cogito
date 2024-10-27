create table IF NOT EXISTS `teste`
(
    id INT auto_increment comment 'identifica o teste'
        primary key,
    status varchar(15) default 'NOVO' not null comment 'pode ser: NOVO, EM_ANDAMENTO, CORRIGIDO ou CANCELADO.',
    data_criacao DATETIME not null comment 'Data da criação do teste',
    data_conclusao DATETIME comment 'Data da conclusão do teste (corrigido ou cancelado)',
    ip varchar(15) not null comment 'IP da máquina que irá receber internet proporcional a nota do teste',
    nota int comment 'Nota obtida no teste'
) ENGINE=InnoDB DEFAULT CHARSET=UTF8
    comment 'Teste aplicado.' auto_increment = 100;

create table IF NOT EXISTS `teste_questao`
(
    id INT auto_increment comment 'identifica a questão no teste' primary key,
    teste_id INT not null comment 'identifica o teste',
    pergunta_id INT not null comment 'identifica a pergunta',
    opcao_id INT comment 'identifica a opcao escolhida',
    constraint teste_questao_fk
        foreign key (teste_id) references teste (id)
            on delete cascade,
    constraint teste_questao_pergunta_fk
        foreign key (pergunta_id) references pergunta (id)
            on delete cascade,
    constraint teste_questao_opcao_fk
        foreign key (opcao_id) references opcao (id)
            on delete cascade

) ENGINE=InnoDB DEFAULT CHARSET=UTF8
    comment 'Questões e gabaritos do teste.' auto_increment = 100;
