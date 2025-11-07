package com.projeto_faculdade.consumidor;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConsumidorRepository extends JpaRepository<Consumidor, Integer> {
    Optional<Consumidor> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
