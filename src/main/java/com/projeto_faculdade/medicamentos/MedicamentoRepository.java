package com.projeto_faculdade.medicamentos;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {

    // ✅ Método correto, existente na sua classe e no banco:
    List<Medicamento> findByNomeContainingIgnoreCase(String nome);

    List<Medicamento> findByInstituicaoId(Integer instituicaoId);

}
