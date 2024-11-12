alter table gabarito
    drop foreign key gabarito_opcao_fk;

alter table gabarito
    drop foreign key gabarito_pergunta_fk;

alter table gabarito
    drop primary key;

alter table gabarito
    add constraint gabarito_pergunta_fk
        foreign key (pergunta_id) references pergunta (id)
            on delete cascade;

alter table gabarito
    add constraint gabarito_opcao_fk
        foreign key (opcao_id) references opcao (id)
            on delete cascade;

alter table gabarito
    add constraint gabarito_uk
        unique (pergunta_id, opcao_id);

alter table gabarito
    auto_increment = 100;

alter table gabarito
    add id INT auto_increment comment 'identifica unicamente o gabarito.'
        primary key;

