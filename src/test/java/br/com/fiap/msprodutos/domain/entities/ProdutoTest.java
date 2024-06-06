package br.com.fiap.msprodutos.domain.entities;

import br.com.fiap.estrutura.exception.BusinessException;
import br.com.fiap.msprodutos.domain.dto.ProdutoDtoResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {

    @Test
    void deveCriarProdutoSemId() throws BusinessException {
        ProdutoEntity produto = new ProdutoEntity("Produto 1", "Descrição do produto 1", 10, 100.0);
        assertNotNull(produto);
    }

    @Test
    void deveCriarProdutoComId() throws BusinessException {
        ProdutoEntity produto = new ProdutoEntity(10, "Produto 1", "Descrição do produto 1", 10, 100.0);
        assertNotNull(produto);
    }

    @Test
    void naoDeveCriarProdutoSemNome() {
        Throwable throwable = assertThrows(BusinessException.class, () -> new ProdutoEntity(null, "Descrição do produto 1", 10, 100.0));
        assertEquals("Nome do produto não pode ser nulo ou vazio", throwable.getMessage());
    }

    @Test
    void naoDeveCriarProdutoSemDescricao() {
        Throwable throwable = assertThrows(BusinessException.class, () -> new ProdutoEntity("Produto 1", null, 10, 100.0));
        assertEquals("Descrição do produto não pode ser nula ou vazia", throwable.getMessage());
    }

    @Test
    void naoDeveCriarProdutoComQuantidadeEstoqueNegativa() {
        Throwable throwable = assertThrows(BusinessException.class, () -> new ProdutoEntity("Produto 1", "Descrição do produto 1", -10, 100.0));
        assertEquals("Quantidade em estoque não pode ser negativa", throwable.getMessage());
    }

    @Test
    void naoDeveCriarProdutoComPrecoNegativoOuZero() {
        Throwable throwable = assertThrows(BusinessException.class, () -> new ProdutoEntity("Produto 1", "Descrição do produto 1", 10, -100.0));
        assertEquals("Preço do produto não pode ser menor ou igual a zero", throwable.getMessage());

        throwable = assertThrows(BusinessException.class, () -> new ProdutoEntity("Produto 1", "Descrição do produto 1", 10, 0));
        assertEquals("Preço do produto não pode ser menor ou igual a zero", throwable.getMessage());
    }

    @Test
    void deveRetornarDto() throws BusinessException {
        ProdutoEntity produto = new ProdutoEntity(10, "Produto 1", "Descrição do produto 1", 10, 100.0);
        ProdutoDtoResponse dto = produto.toDto();
        assertNotNull(produto);
        assertNotNull(dto);
        assertEquals(produto.getId(), dto.id());
        assertEquals(produto.getNome(), dto.nome());
        assertEquals(produto.getDescricao(), dto.descricao());
        assertEquals(produto.getQuantidadeEstoque(), dto.quantidadeEstoque());
        assertEquals(produto.getPreco(), dto.preco());
    }

    @Test
    void deveCriarProdutoEntitySemValores() {
        ProdutoEntity produto = new ProdutoEntity();
        assertNotNull(produto);
    }
}
