package br.com.fiap.msprodutos.domain.dto;

public record ProdutoDtoResponse(
        int id,
        String nome,
        String descricao,
        int quantidadeEstoque,
        double preco
) {
}