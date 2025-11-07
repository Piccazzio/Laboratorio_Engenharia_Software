package com.projeto_faculdade.medicamentos;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@CrossOrigin(origins = "*")
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository repository;

    // 🔹 Listar todos os medicamentos
    @GetMapping
    public List<Medicamento> listarTodos() {
        return repository.findAll();
    }

    // 🔹 Buscar medicamento por ID
    @GetMapping("/{id}")
    public Medicamento buscarPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento não encontrado"));
    }

    // 🔹 Buscar medicamento por nome (parcial, ex: ?nome=paracetamol)
    @GetMapping("/buscar")
    public List<Medicamento> buscarPorNome(@RequestParam String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    // 🔹 Criar novo medicamento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Medicamento criar(@RequestBody Medicamento medicamento) {
        return repository.save(medicamento);
    }

    // 🔹 Atualizar medicamento existente
    @PutMapping("/{id}")
    public Medicamento atualizar(@PathVariable Integer id, @RequestBody Medicamento dados) {
        Medicamento m = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento não encontrado"));

        if (dados.getNome() != null) m.setNome(dados.getNome());
        if (dados.getLote() != null) m.setLote(dados.getLote());
        if (dados.getValidade() != null) m.setValidade(dados.getValidade());
        if (dados.getPreco() != null) m.setPreco(dados.getPreco());
        if (dados.getQuantidade() != null) m.setQuantidade(dados.getQuantidade());
        if (dados.getFotoUrl() != null) m.setFotoUrl(dados.getFotoUrl());

        return repository.save(m);
    }

    // 🔹 Deletar medicamento
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento não encontrado");
        }
        repository.deleteById(id);
    }
}
