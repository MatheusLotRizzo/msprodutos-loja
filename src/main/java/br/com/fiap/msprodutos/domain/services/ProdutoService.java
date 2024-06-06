package br.com.fiap.msprodutos.domain.services;

import br.com.fiap.estrutura.exception.BusinessException;
import br.com.fiap.msprodutos.domain.dto.ProdutoDtoRequest;
import br.com.fiap.msprodutos.domain.dto.ProdutoDtoResponse;
import br.com.fiap.msprodutos.domain.entities.ProdutoEntity;
import br.com.fiap.msprodutos.domain.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoDtoResponse> listarProdutos(){
        return produtoRepository.findAll().stream().map(ProdutoEntity::toDto).toList();
    }

    public ProdutoDtoResponse buscarProdutoPorId(int id) throws BusinessException {
        ProdutoEntity produtoEntity = buscarProdutoEntity(id);
        return produtoEntity.toDto();
    }

    public ProdutoDtoResponse cadastrarProduto(ProdutoDtoRequest produto) throws BusinessException {
        ProdutoEntity produtoEntity = produtoRepository.findByNome(produto.nome());
        if(produtoEntity != null){
            throw new BusinessException("Já existe produto cadastrado com esse nome");
        }
        return produtoRepository.save(produto.toEntity()).toDto();
    }

    public ProdutoDtoResponse atualizarProduto(Integer id, ProdutoDtoRequest produtoDto) throws BusinessException {
        ProdutoEntity produtoExistente = buscarProdutoEntity(id);

        ProdutoEntity produtoAtualizado = new ProdutoEntity(
                produtoExistente.getId(),
                produtoDto.nome(),
                produtoDto.descricao(),
                produtoDto.quantidadeEstoque(),
                produtoDto.preco()
        );
        return produtoRepository.save(produtoAtualizado).toDto();
    }

    public void excluirProduto(int id) throws BusinessException {
        buscarProdutoEntity(id);
        produtoRepository.deleteById(id);
    }

    public ProdutoDtoResponse acrescentarEstoque(Integer id, int quantidade) throws BusinessException {
        ProdutoEntity produto = buscarProdutoEntity(id);
        ProdutoEntity produtoAtualizado = new ProdutoEntity(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getQuantidadeEstoque() + quantidade,
                produto.getPreco()
        );
        return produtoRepository.save(produtoAtualizado).toDto();
    }

    public ProdutoDtoResponse diminuirEstoque(Integer id, int quantidade) throws BusinessException {
        ProdutoEntity produto = buscarProdutoEntity(id);
        ProdutoEntity produtoAtualizado = new ProdutoEntity(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getQuantidadeEstoque() - quantidade,
                produto.getPreco()
        );
        return produtoRepository.save(produtoAtualizado).toDto();
    }

    private ProdutoEntity buscarProdutoEntity(int id) throws BusinessException {
        ProdutoEntity produto = produtoRepository.findById(id).orElse(null);
        if(produto == null){
            throw new BusinessException("Produto não encontrado");
        }
        return produto;
    }
}