package com.projeto_faculdade.pedido;

import com.projeto_faculdade.consumidor.Consumidor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByConsumidor(Consumidor consumidor);
}
