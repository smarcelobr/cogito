SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE teste_questao;
TRUNCATE teste;
TRUNCATE gabarito;
TRUNCATE opcao;
TRUNCATE pergunta;
TRUNCATE cota;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO cota (id, ip, end_time) VALUES (100, '192.168.2.10', '2024-10-28 19:00:00');

INSERT INTO pergunta (id, disciplina, questao) VALUES (100, 'Cálculo algébrico', '\\text{Qual das opções é igual à expressão }(3x-2)^2\\text{ ?}');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (100, 100, '(3x-2) + (3x-2)');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (101, 100, '(3x-2)(3x-2)');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (102, 100, '9x^2-4');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (103, 100, '(3x-2) - (3x-2)');
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (100, 100, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (100, 101, 1, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (100, 102, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (100, 103, 0, null);

INSERT INTO pergunta (id, disciplina, questao) VALUES (101, 'Cálculo algébrico', '\\text{Qual das opções é igual à expressão }2(3x-2)\\text{ ?}');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (104, 101, '(3x-2) + (3x-2)');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (105, 101, '(3x-2)(3x-2)');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (106, 101, '9x^2-4');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (107, 101, '(3x-2) - (3x-2)');
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (101, 104, 1, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (101, 105, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (101, 106, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (101, 107, 0, null);

INSERT INTO teste (id, status, data_criacao, data_conclusao, ip, nota) VALUES (1, 'EM_ANDAMENTO', '2024-10-27 10:17:09', null, '192.162.2.10', null);

INSERT INTO teste_questao (id, teste_id, pergunta_id, opcao_id) VALUES (1, 1, 100, 102);
INSERT INTO teste_questao (id, teste_id, pergunta_id, opcao_id) VALUES (2, 1, 101, 105);