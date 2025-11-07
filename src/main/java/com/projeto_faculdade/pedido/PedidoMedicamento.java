package com.projeto_faculdade.pedido;

import com.projeto_faculdade.medicamentos.Medicamento;
import jakarta.persistence.*;

@Entity
@Table(name = "pedido_medicamento")
public class PedidoMedicamento {

    @EmbeddedId
    private PedidoMedicamentoId id = new PedidoMedicamentoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pedidoId")
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicamentoId")
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;

    @Column(nullable = false)
    private Integer quantidade = 1;

    // Getters e Setters
    public PedidoMedicamentoId getId() { return id; }
    public void setId(PedidoMedicamentoId id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
}
