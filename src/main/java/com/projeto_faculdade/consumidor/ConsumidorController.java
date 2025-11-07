package com.projeto_faculdade.consumidor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;
import com.projeto_faculdade.dto.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consumidor")
@CrossOrigin(origins = "*")
public class ConsumidorController {

    @Autowired
    private ConsumidorRepository repository;

    private final String uploadDir = "src/main/resources/static/uploads/consumidores/";

    @GetMapping
    public List<Consumidor> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Consumidor buscarPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consumidor não encontrado"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Consumidor criar(@RequestBody Consumidor consumidor) {
        if (repository.existsByCpf(consumidor.getCpf())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado");
        }
        return repository.save(consumidor);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Consumidor> consumidor = repository.findByEmail(request.getEmail());

        if (consumidor.isPresent() && consumidor.get().getSenha().equals(request.getSenha())) {
            return ResponseEntity.ok(consumidor.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("E-mail ou senha inválidos");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Consumidor> atualizarConsumidor(
            @PathVariable Integer id,
            @RequestBody Consumidor dadosAtualizados) {

        Optional<Consumidor> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Consumidor c = optional.get();
        c.setNome(dadosAtualizados.getNome());
        c.setSenha(dadosAtualizados.getSenha());
        c.setTelefone(dadosAtualizados.getTelefone());
        c.setEndereco(dadosAtualizados.getEndereco());
        c.setFotoUrl(dadosAtualizados.getFotoUrl());
        repository.save(c);

        return ResponseEntity.ok(c);
    }

    @PostMapping("/{id}/foto")
    public Consumidor uploadFoto(@PathVariable Integer id, @RequestParam("foto") MultipartFile foto) {
        Consumidor consumidor = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consumidor não encontrado"));

        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String fileName = UUID.randomUUID() + "_" + foto.getOriginalFilename();
        File dest = new File(uploadDir + fileName);

        try {
            foto.transferTo(dest);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar a imagem");
        }

        String fotoUrl = "/uploads/consumidores/" + fileName;
        consumidor.setFotoUrl(fotoUrl);
        return repository.save(consumidor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Consumidor não encontrado");
        }
        repository.deleteById(id);
    }
}
