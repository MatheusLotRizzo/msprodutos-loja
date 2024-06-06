package br.com.fiap.msprodutos.domain.controllers;

import br.com.fiap.estrutura.swagger.annotations.ApiResponseSwaggerCreate;
import br.com.fiap.estrutura.swagger.annotations.ApiResponseSwaggerNoContent;
import br.com.fiap.estrutura.swagger.annotations.ApiResponseSwaggerOk;
import br.com.fiap.estrutura.utils.SpringControllerUtils;
import br.com.fiap.msprodutos.domain.dto.ProdutoDtoRequest;
import br.com.fiap.msprodutos.domain.services.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Produtos", description = "Rotas para gerenciamento dos produtos")
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    @Operation(summary = "Listar todos os produtos")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> listarProdutos() {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.listarProdutos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> buscarProdutoPorId(@PathVariable int id) {
        return SpringControllerUtils.response(HttpStatus.OK, () -> produtoService.buscarProdutoPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo produto")
    @ApiResponseSwaggerCreate
    public ResponseEntity<?> cadastrarProduto(@RequestBody ProdutoDtoRequest produto) {
        return SpringControllerUtils.response(HttpStatus.CREATED, () -> produtoService.cadastrarProduto(produto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um produto")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> atualizarProduto(@PathVariable Integer id, @RequestBody ProdutoDtoRequest produto) {
        return SpringControllerUtils.response(HttpStatus.OK, () -> produtoService.atualizarProduto(id, produto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um produto")
    @ApiResponseSwaggerNoContent
    public ResponseEntity<?> excluirProduto(@PathVariable int id) {
        return SpringControllerUtils.response(HttpStatus.NO_CONTENT, () -> {
            produtoService.excluirProduto(id);
            return null;
        });
    }

    @PutMapping("/{id}/estoque/acrescentar/{quantidade}")
    @Operation(summary = "Acrescentar quantidade ao estoque")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> acrescentarEstoque(@PathVariable Integer id, @PathVariable int quantidade) {
        return SpringControllerUtils.response(HttpStatus.OK, () -> produtoService.acrescentarEstoque(id, quantidade));
    }

    @PutMapping("/{id}/estoque/diminuir/{quantidade}")
    @Operation(summary = "Diminuir quantidade do estoque")
    @ApiResponseSwaggerOk
    public ResponseEntity<?> diminuirEstoque(@PathVariable Integer id, @PathVariable int quantidade) {
        return SpringControllerUtils.response(HttpStatus.OK, () -> produtoService.diminuirEstoque(id, quantidade));
    }
}