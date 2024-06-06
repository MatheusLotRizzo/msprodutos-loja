package br.com.fiap.msprodutos.domain.services;

import br.com.fiap.estrutura.exception.BusinessException;
import br.com.fiap.msprodutos.domain.dto.ProdutoDtoRequest;
import br.com.fiap.msprodutos.domain.dto.ProdutoDtoResponse;
import br.com.fiap.msprodutos.domain.entities.ProdutoEntity;
import br.com.fiap.msprodutos.domain.repositories.ProdutoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.CollectionAssert.assertThatCollection;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    ProdutoService service;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void deveRetornarListaDeProdutos() throws BusinessException {
        ProdutoEntity produto1 = new ProdutoEntity(1, "Produto", "Descrição", 10, 100.0);
        ProdutoEntity produto2 = new ProdutoEntity(2, "Produto2", "Descrição2", 15, 100.0);
        when(repository.findAll()).thenReturn(Arrays.asList(produto1, produto2));

        final List<ProdutoDtoResponse> produtos = service.listarProdutos();
        assertNotNull(produtos);
        assertThatCollection(produtos)
                .hasSize(2)
                .extracting(ProdutoDtoResponse::id)
                .contains(1, 2);
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveRetornarArrayVazioQuandoNaoTiverProdutos() throws BusinessException {
        when(repository.findAll()).thenReturn(List.of());
        final List<ProdutoDtoResponse> produtos = service.listarProdutos();
        assertNotNull(produtos);
        assertThatCollection(produtos).isEmpty();
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveRetornarProdutoPorId() throws BusinessException {
        ProdutoEntity produto = new ProdutoEntity(1, "Produto", "Descrição", 10, 100.0);
        when(repository.findById(anyInt())).thenReturn(Optional.of(produto));

        final ProdutoDtoResponse produtoDto = service.buscarProdutoPorId(1);
        assertNotNull(produtoDto);
        verify(repository, times(1)).findById(anyInt());
    }

    @Test
    void naoDeveRetornarProdutoPorIdInexistente() {
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.buscarProdutoPorId(anyInt()));
        assertEquals("Produto não encontrado", throwable.getMessage());
    }

    @Test
    void deveCadastrarProduto() throws BusinessException {
        ProdutoDtoRequest produtoDtoRequest = new ProdutoDtoRequest("Produto", "Descrição", 10, 100.0);
        ProdutoEntity produto = produtoDtoRequest.toEntity();

        when(repository.findByNome(anyString())).thenReturn(null);
        when(repository.save(any(ProdutoEntity.class))).thenReturn(produto);

        final ProdutoDtoResponse produtoDto = service.cadastrarProduto(produtoDtoRequest);
        assertNotNull(produtoDto);
        assertEquals(produto.getNome(), produtoDto.nome());
        assertEquals(produto.getDescricao(), produtoDto.descricao());
        assertEquals(produto.getQuantidadeEstoque(), produtoDto.quantidadeEstoque());
        assertEquals(produto.getPreco(), produtoDto.preco());
        verify(repository, times(1)).findByNome(anyString());
        verify(repository, times(1)).save(any(ProdutoEntity.class));
    }

    @Test
    void naoDeveCadastrarProdutoExistente() throws BusinessException {
        ProdutoDtoRequest produtoDtoRequest = new ProdutoDtoRequest("Produto", "Descrição", 10, 100.0);
        ProdutoEntity produto = produtoDtoRequest.toEntity();

        when(repository.findByNome(anyString())).thenReturn(produto);

        final Throwable throwable = assertThrows(BusinessException.class, () -> service.cadastrarProduto(produtoDtoRequest));
        assertEquals("Já existe produto cadastrado com esse nome", throwable.getMessage());
        verify(repository, times(1)).findByNome(anyString());
        verify(repository, never()).save(any(ProdutoEntity.class));
    }

    @Test
    void deveAtualizarProduto() throws BusinessException {
        ProdutoEntity produto = new ProdutoEntity(1, "Produto", "Descrição", 10, 100.0);
        ProdutoDtoRequest produtoDtoRequest = new ProdutoDtoRequest("Produto", "Descrição", 10, 100.0);

        when(repository.findById(anyInt())).thenReturn(Optional.of(produto));
        when(repository.save(any(ProdutoEntity.class))).thenReturn(produto);

        final ProdutoDtoResponse produtoDto = service.atualizarProduto(produto.getId(), produtoDtoRequest);
        assertNotNull(produtoDto);
        assertEquals(produto.getNome(), produtoDto.nome());
        assertEquals(produto.getDescricao(), produtoDto.descricao());
        assertEquals(produto.getQuantidadeEstoque(), produtoDto.quantidadeEstoque());
        assertEquals(produto.getPreco(), produtoDto.preco());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(1)).save(any(ProdutoEntity.class));
    }

    @Test
    void naoDeveAtualizarProdutoInexistente() {
        ProdutoDtoRequest produtoDtoRequest = new ProdutoDtoRequest("Produto", "Descrição", 10, 100.0);

        final Throwable throwable = assertThrows(BusinessException.class, () -> service.atualizarProduto(anyInt(), produtoDtoRequest));
        assertEquals("Produto não encontrado", throwable.getMessage());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, never()).save(any(ProdutoEntity.class));
    }

    @Test
    void deveExcluirProduto() throws BusinessException {
        ProdutoEntity produto = new ProdutoEntity(1, "Produto", "Descrição", 10, 100.0);
        when(repository.findById(anyInt())).thenReturn(Optional.of(produto));
        service.excluirProduto(produto.getId());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(1)).deleteById(anyInt());
    }

    @Test
    void naoDeveExcluirProdutoInexistente() {
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.excluirProduto(anyInt()));
        assertEquals("Produto não encontrado", throwable.getMessage());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    void deveAcrescentarEstoque() throws BusinessException {
        ProdutoEntity produtoAntigo = new ProdutoEntity(1, "Produto", "Descrição", 10, 100.0);
        ProdutoEntity produtoAtualizado = new ProdutoEntity(1, "Produto", "Descrição", 15, 100.0);
        when(repository.findById(anyInt())).thenReturn(Optional.of(produtoAntigo));
        when(repository.save(any(ProdutoEntity.class))).thenReturn(produtoAtualizado);

        final ProdutoDtoResponse produtoDto = service.acrescentarEstoque(produtoAntigo.getId(), 5);
        assertNotNull(produtoDto);
        assertEquals(produtoAntigo.getQuantidadeEstoque() + 5, produtoDto.quantidadeEstoque());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(1)).save(any(ProdutoEntity.class));
    }

    @Test
    void naoDeveAcrescentarEstoqueProdutoInexistente() {
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.acrescentarEstoque(anyInt(), 5));
        assertEquals("Produto não encontrado", throwable.getMessage());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, never()).save(any(ProdutoEntity.class));
    }

    @Test
    void deveDiminuirEstoque() throws BusinessException {
        ProdutoEntity produtoAntigo = new ProdutoEntity(1, "Produto", "Descrição", 10, 100.0);
        ProdutoEntity produtoAtualizado = new ProdutoEntity(1, "Produto", "Descrição", 5, 100.0);
        when(repository.findById(anyInt())).thenReturn(Optional.of(produtoAntigo));
        when(repository.save(any(ProdutoEntity.class))).thenReturn(produtoAtualizado);

        final ProdutoDtoResponse produtoDto = service.diminuirEstoque(produtoAntigo.getId(), 5);
        assertNotNull(produtoDto);
        assertEquals(produtoAntigo.getQuantidadeEstoque() - 5, produtoDto.quantidadeEstoque());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, times(1)).save(any(ProdutoEntity.class));
    }

    @Test
    void naoDeveDiminuirEstoqueProdutoInexistente() {
        final Throwable throwable = assertThrows(BusinessException.class, () -> service.diminuirEstoque(anyInt(), 5));
        assertEquals("Produto não encontrado", throwable.getMessage());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, never()).save(any(ProdutoEntity.class));
    }
}