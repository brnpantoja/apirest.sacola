package me.dio.sacola.model;

public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Produto produto;
    private int quantidade;
    @ManyToOne
    @JasonIgnore
    private Sacola sacola;
}
