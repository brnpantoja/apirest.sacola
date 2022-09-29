INSERT INTO restaurante (id, cep, complemento, nome) VALUES
(1L, 'O000001', 'Complemento Endereço Restaurante 1', 'Restaurante 1'),
(2L, 'O000002', 'Complemento Endereço Restaurante 2', 'Restaurante 2');

INSERT INTO cliente (id, cep, complemento, nome) VALUES
(1L, 'O000001', 'Complemento Endereço Cliente 1', 'Cliente 1'),
(2L, 'O000002', 'Complemento Endereço Cliente 2', 'Cliente 2');

INSERT INTO produto (id, disponivel, nome, valor_unitario, restaurante_id) VALUES
(1L, true, 'Produto 1', 5.0, 1L),
(2L, true, 'Produto 2', 6.0, 1L),
(3L, true, 'Produto 3', 7.0, 2L);

INSERT INTO sacola (id, forma_pagamento, fechada, valor_total, cliente_id) VALUES
(1L, 0, false, 0.0, 1L);