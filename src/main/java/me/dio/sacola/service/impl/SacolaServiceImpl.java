package me.dio.sacola.service.impl;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Restaurante;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.repository.ItemRepository;
import me.dio.sacola.repository.ProdutoRepository;
import me.dio.sacola.repository.SacolaRepository;
import me.dio.sacola.resource.dto.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SacolaServiceImpl implements SacolaService {
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemRepository itemRepository;

    @Override
    public Item incluirItemNaSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getIdSacola());

        if (sacola.isFechada()) {
            throw new RuntimeException("Sacola já está fechada");
        }

        Item itemParaSerInserido = Item.builder()
                .quantidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Produto não encontrado");
                        }
                ))
                .build();

        List<Item> itensDaSacola = sacola.getItens();
        if (itensDaSacola.isEmpty()) {
            itensDaSacola.add(itemParaSerInserido);
        } else {
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteDoItemParaAdicionar = itemParaSerInserido.getProduto().getRestaurante();
            if (restauranteAtual.equals(restauranteDoItemParaAdicionar)) {
                itensDaSacola.add(itemParaSerInserido);
            } else {
                throw new RuntimeException("Não é possível adicionar itens de restaurantes diferentes. " +
                        "Feche a sacola e abra outra.");
            }
        }

        List<Double> valorDosItens = new ArrayList<>();
        for (Item itemDaSacola : itensDaSacola) {
            double valorTotalItem =
                    itemDaSacola.getProduto().getValorUnitario() * itemDaSacola.getQuantidade();
            valorDosItens.add(valorTotalItem);
        }
//
//        Double valorTotalSacola = 0.0;
//        for (Double valorDeCadaItem : valorDosItens) {
//            valorTotalSacola += valorDeCadaItem;

        double valorTotalSacola = valorDosItens.stream()
                .mapToDouble(valorTotalDeCadaItem -> valorTotalDeCadaItem)
                .sum();

        sacola.setValorTotal(valorTotalSacola);
        sacolaRepository.save(sacola);
        return itemParaSerInserido;
    }

    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Sacola não encontrada");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int numeroformaPagamento) {
        Sacola sacola = verSacola(id);
        if (sacola.getItens().isEmpty()) {
            throw new RuntimeException("Não é possível fechar uma sacola vazia");
        }
        if (numeroformaPagamento == 0) {
            sacola.setFormaPagamento((FormaPagamento.DINHEIRO));
        } else {
            sacola.setFormaPagamento((FormaPagamento.MAQUINETA));
        }

        sacola.setFechada(true);
        return sacolaRepository.save(sacola);

    }

    @Override
    public Sacola deletarSacola(Long id) {
        Sacola sacola = verSacola(id);
        if (sacola.isFechada()) {
            throw new RuntimeException("Não é possível deletar uma sacola fechada");
        }
        for (Item item : sacola.getItens()) {
            itemRepository.delete(item);
        }
        sacolaRepository.delete(sacola);
        return sacola;
    }
}

