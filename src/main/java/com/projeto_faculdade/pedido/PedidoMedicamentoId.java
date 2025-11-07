package com.projeto_faculdade.pedido;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PedidoMedicamentoId implements Serializable {

    private Integer pedidoId;
    private Integer medicamentoId;

    public PedidoMedicamentoId() {}

    public PedidoMedicamentoId(Integer pedidoId, Integer medicamentoId) {
        this.pedidoId = pedidoId;
        this.medicamentoId = medicamentoId;
    }

    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }

    public Integer getMedicamentoId() { return medicamentoId; }
    public void setMedicamentoId(Integer medicamentoId) { this.medicamentoId = medicamentoId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PedidoMedicamentoId)) return false;
        PedidoMedicamentoId that = (PedidoMedicamentoId) o;
        return Objects.equals(pedidoId, that.pedidoId) && Objects.equals(medicamentoId, that.medicamentoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoId, medicamentoId);
    }
}
