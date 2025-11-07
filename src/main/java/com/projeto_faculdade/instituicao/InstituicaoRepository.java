package com.projeto_faculdade.instituicao;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InstituicaoRepository extends JpaRepository<Instituicao, Integer> {
    Optional<Instituicao> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);
}
