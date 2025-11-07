package com.projeto_faculdade.pedido;

import com.projeto_faculdade.medicamentos.Medicamento;
import com.projeto_faculdade.medicamentos.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos/{pedidoId}/medicamentos")
@CrossOrigin(origins = "*")
public class PedidoMedicamentoController {

    @Autowired
    private PedidoMedicamentoRepository pedidoMedicamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping
    public List<PedidoMedicamento> listarItens(@PathVariable int pedidoId) {
        return pedidoMedicamentoRepository.findAll()
                .stream()
                .filter(pm -> pm.getPedido().getId() == pedidoId)
                .toList();
    }

    @PostMapping
    public PedidoMedicamento adicionarItem(@PathVariable int pedidoId, @RequestParam int medicamentoId, @RequestParam int quantidade) {
        var pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));

        var medicamento = medicamentoRepository.findById(medicamentoId)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado."));

        PedidoMedicamento item = new PedidoMedicamento();
        item.setPedido(pedido);
        item.setMedicamento(medicamento);
        item.setQuantidade(quantidade);

        return pedidoMedicamentoRepository.save(item);
    }

    @PutMapping("/{medicamentoId}")
    public PedidoMedicamento atualizarQuantidade(@PathVariable int pedidoId, @PathVariable int medicamentoId, @RequestParam int quantidade) {
        var item = pedidoMedicamentoRepository.findAll()
                .stream()
                .filter(pm -> pm.getPedido().getId() == pedidoId && pm.getMedicamento().getId() == medicamentoId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado."));

        item.setQuantidade(quantidade);
        return pedidoMedicamentoRepository.save(item);
    }

    @DeleteMapping("/{medicamentoId}")
    public void removerItem(@PathVariable int pedidoId, @PathVariable int medicamentoId) {
        var item = pedidoMedicamentoRepository.findAll()
                .stream()
                .filter(pm -> pm.getPedido().getId() == pedidoId && pm.getMedicamento().getId() == medicamentoId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item não encontrado."));

        pedidoMedicamentoRepository.delete(item);
    }
}
