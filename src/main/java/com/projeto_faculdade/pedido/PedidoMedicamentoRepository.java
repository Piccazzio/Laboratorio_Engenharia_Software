package com.projeto_faculdade.pedido;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoMedicamentoRepository extends JpaRepository<PedidoMedicamento, PedidoMedicamentoId> {
}
