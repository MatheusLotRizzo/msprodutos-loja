package br.com.fiap.msprodutos.domain.dto;

import br.com.fiap.estrutura.exception.BusinessException;
import br.com.fiap.msprodutos.domain.entities.ProdutoEntity;

public record ProdutoDtoRequest(
        String nome,
        String descricao,
        int quantidadeEstoque,
        double preco
) {
    public ProdutoEntity toEntity() throws BusinessException {
        return new ProdutoEntity(nome, descricao, quantidadeEstoque, preco);
    }
}