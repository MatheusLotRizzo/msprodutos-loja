package br.com.fiap.msprodutos.domain.repositories;

import br.com.fiap.msprodutos.domain.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Integer> {
    ProdutoEntity findByNome(String nome);
}
