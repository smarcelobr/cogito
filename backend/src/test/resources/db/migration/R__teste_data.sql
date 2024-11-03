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

INSERT INTO pergunta (id, disciplina, questao) VALUES (10, 'Produto notável', '\\text{O produto da soma pela diferença de dois termos }(x+y)(x-y)\\text{ é igual a:}');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (108, 10, 'x^2 - 2xy + y^2');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (109, 10, '2x-2y');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (110, 10, 'x^2-y^2');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (111, 10, '2xy-2yx');
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (10, 108, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (10, 109, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (10, 110, 1, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (10, 111, 0, null);

INSERT INTO pergunta (id, disciplina, questao) VALUES (11, 'Produto notável', '\\text{O quadrado da subtração de dois termos }(x-y)^2\\text{ é igual a:}');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (112, 11, 'x^2 - y^2');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (113, 11, 'x^2 + 2xy - y^2');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (114, 11, 'x^2 - 2xy - y^2');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (115, 11, 'x^2 - 2xy + y^2');
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (11, 112, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (11, 113, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (11, 114, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (11, 115, 1, null);

INSERT INTO pergunta (id, disciplina, questao) VALUES (12, 'Produto notável', '\\text{O quadrado da soma de dois termos }(x+y)^2\\text{ ou }(x+y)\\cdot(x+y)\\text{ é igual a:}');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (116, 12, 'x^2 + y^2');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (117, 12, 'x^2 + 2xy - y^2');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (118, 12, 'x^2 + 2xy + y^2');
INSERT INTO opcao (id, pergunta_id, alternativa) VALUES (119, 12, 'x^2 - 2xy + y^2');
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (12, 116, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (12, 117, 0, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (12, 118, 1, null);
INSERT INTO gabarito (pergunta_id, opcao_id, correta, explicacao) VALUES (12, 119, 0, null);

INSERT INTO teste (id, status, data_criacao, data_conclusao, ip, nota) VALUES (1, 'EM_ANDAMENTO', '2024-10-27 10:17:09', null, '192.162.2.10', null);

INSERT INTO teste_questao (id, teste_id, peso, pergunta_id, opcao_id) VALUES (1, 1, 1,100, 102);
INSERT INTO teste_questao (id, teste_id, peso, pergunta_id, opcao_id) VALUES (2, 1, 1,101, 105);
