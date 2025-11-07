package com.projeto_faculdade.instituicao;

import com.projeto_faculdade.medicamentos.Medicamento;
import com.projeto_faculdade.medicamentos.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/instituicao/medicamentos")
@CrossOrigin(origins = "*")
public class InstituicaoMedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private InstituicaoRepository instituicaoRepository;

    // 🔹 Listar todos os medicamentos da instituição logada
    @GetMapping("/{instituicaoId}")
    public List<Medicamento> listarPorInstituicao(@PathVariable Integer instituicaoId) {
        var instituicao = instituicaoRepository.findById(instituicaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada"));
        return medicamentoRepository.findByInstituicaoId(instituicao.getId());
    }

    // 🔹 Criar um novo medicamento para a instituição
    @PostMapping("/{instituicaoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Medicamento criar(@PathVariable Integer instituicaoId, @RequestBody Medicamento medicamento) {
        var instituicao = instituicaoRepository.findById(instituicaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada"));

        medicamento.setInstituicao(instituicao); // Associa o medicamento à instituição
        return medicamentoRepository.save(medicamento);
    }

    // 🔹 Atualizar um medicamento da instituição
    @PutMapping("/{instituicaoId}/{medicamentoId}")
    public Medicamento atualizar(@PathVariable Integer instituicaoId, 
                                 @PathVariable Integer medicamentoId, 
                                 @RequestBody Medicamento dados) {
        var instituicao = instituicaoRepository.findById(instituicaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada"));

        var medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento não encontrado"));

        if (!medicamento.getInstituicao().getId().equals(instituicao.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este medicamento não pertence à instituição logada");
        }

        // Atualiza apenas os campos recebidos
        if (dados.getNome() != null) medicamento.setNome(dados.getNome());
        if (dados.getLote() != null) medicamento.setLote(dados.getLote());
        if (dados.getValidade() != null) medicamento.setValidade(dados.getValidade());
        if (dados.getPreco() != null) medicamento.setPreco(dados.getPreco());
        if (dados.getQuantidade() != null) medicamento.setQuantidade(dados.getQuantidade());
        if (dados.getFotoUrl() != null) medicamento.setFotoUrl(dados.getFotoUrl());

        return medicamentoRepository.save(medicamento);
    }

    // 🔹 Deletar medicamento da instituição
    @DeleteMapping("/{instituicaoId}/{medicamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer instituicaoId, @PathVariable Integer medicamentoId) {
        var instituicao = instituicaoRepository.findById(instituicaoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada"));

        var medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento não encontrado"));

        if (!medicamento.getInstituicao().getId().equals(instituicao.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Este medicamento não pertence à instituição logada");
        }

        medicamentoRepository.delete(medicamento);
    }
}
