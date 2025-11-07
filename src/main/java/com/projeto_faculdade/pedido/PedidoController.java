package com.projeto_faculdade.pedido;

import com.projeto_faculdade.consumidor.Consumidor;
import com.projeto_faculdade.consumidor.ConsumidorRepository;
import com.projeto_faculdade.medicamentos.Medicamento;
import com.projeto_faculdade.medicamentos.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoMedicamentoRepository pedidoMedicamentoRepository;

    @Autowired
    private ConsumidorRepository consumidorRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pedido buscarPorId(@PathVariable int id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Pedido criarPedido(@RequestBody PedidoRequestDTO dto) {
        Consumidor consumidor = consumidorRepository.findById(dto.getConsumidorId())
                .orElseThrow(() -> new RuntimeException("Consumidor não encontrado."));

        Pedido pedido = new Pedido();
        pedido.setConsumidor(consumidor);
        pedido.setData(LocalDateTime.now());
        pedido.setStatus("PENDENTE");
        pedidoRepository.save(pedido);

        for (PedidoRequestDTO.ItemPedidoDTO item : dto.getItens()) {
            Medicamento medicamento = medicamentoRepository.findById(item.getMedicamentoId())
                    .orElseThrow(() -> new RuntimeException("Medicamento não encontrado."));

            PedidoMedicamento pedidoMedicamento = new PedidoMedicamento();
            pedidoMedicamento.setPedido(pedido);
            pedidoMedicamento.setMedicamento(medicamento);
            pedidoMedicamento.setQuantidade(item.getQuantidade());

            pedidoMedicamentoRepository.save(pedidoMedicamento);
        }

        return pedido;
    }

    @PutMapping("/{id}/status")
    public Pedido atualizarStatus(@PathVariable int id, @RequestParam String status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }

    @DeleteMapping("/{id}")
    public void excluirPedido(@PathVariable int id) {
        pedidoRepository.deleteById(id);
    }
}
