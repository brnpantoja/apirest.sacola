package me.dio.sacola.model;

import me.dio.sacola.enumeration.FormaPagamento;

import javax.persistence.Enumerated;

public class Sacola {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    private Cliente cliente;
    @OneToMan(cascade = CascadeType.All)
    private List<Item> itens;
    private Double valorTotal;
    @Enumerated
    private FormaPagamento formaPagamento;
    private boolean fechada;

}
