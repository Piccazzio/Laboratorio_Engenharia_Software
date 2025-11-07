package com.projeto_faculdade.pedido;

import java.util.List;

public class PedidoRequestDTO {
    private int consumidorId;
    private List<ItemPedidoDTO> itens;

    public int getConsumidorId() {
        return consumidorId;
    }

    public void setConsumidorId(int consumidorId) {
        this.consumidorId = consumidorId;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }

    public static class ItemPedidoDTO {
        private int medicamentoId;
        private int quantidade;

        public int getMedicamentoId() {
            return medicamentoId;
        }

        public void setMedicamentoId(int medicamentoId) {
            this.medicamentoId = medicamentoId;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }
    }
}
