package com.projeto_faculdade.instituicao;

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
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/instituicao")
@CrossOrigin(origins = "*")
public class InstituicaoController {

    @Autowired
    private InstituicaoRepository repository;

    private final String uploadDir = "src/main/resources/static/uploads/instituicoes/";

    @GetMapping
    public List<Instituicao> listarTodos() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Instituicao buscarPorId(@PathVariable Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Instituicao criar(@RequestBody Instituicao inst) {
        if (repository.existsByCnpj(inst.getCnpj())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ já cadastrado");
        }
        return repository.save(inst);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Instituicao> instituicao = repository.findByEmail(request.getEmail());

        if (instituicao.isPresent() && instituicao.get().getSenha().equals(request.getSenha())) {
            return ResponseEntity.ok(instituicao.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("E-mail ou senha inválidos");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instituicao> atualizarInstituicao(
            @PathVariable Integer id,
            @RequestBody Instituicao dadosAtualizados) {

        Optional<Instituicao> optional = repository.findById(id);
        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Instituicao i = optional.get();
        i.setNome(dadosAtualizados.getNome());
        i.setSenha(dadosAtualizados.getSenha());
        i.setTelefone(dadosAtualizados.getTelefone());
        i.setEndereco(dadosAtualizados.getEndereco());
        i.setFotoUrl(dadosAtualizados.getFotoUrl());
        repository.save(i);

        return ResponseEntity.ok(i);
    }

    @PostMapping("/{id}/foto")
    public Instituicao uploadFoto(@PathVariable Integer id, @RequestParam("foto") MultipartFile foto) {
        Instituicao inst = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada"));

        // Cria diretório se não existir
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Gera nome único
        String fileName = UUID.randomUUID() + "_" + foto.getOriginalFilename();
        File dest = new File(uploadDir + fileName);

        try {
            foto.transferTo(dest);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar a imagem");
        }

        // Define URL pública (pode ajustar conforme seu servidor)
        String fotoUrl = "/uploads/instituicoes/" + fileName;
        inst.setFotoUrl(fotoUrl);
        return repository.save(inst);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada");
        }
        repository.deleteById(id);
    }
}
