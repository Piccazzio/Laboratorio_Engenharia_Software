package com.projeto_faculdade.pedido;

import com.projeto_faculdade.consumidor.Consumidor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "consumidor_id", nullable = false)
    private Consumidor consumidor;

    @Column(name = "data", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime data = LocalDateTime.now();

    @Column(length = 50)
    private String status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoMedicamento> medicamentos = new ArrayList<>();

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Consumidor getConsumidor() { return consumidor; }
    public void setConsumidor(Consumidor consumidor) { this.consumidor = consumidor; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<PedidoMedicamento> getMedicamentos() { return medicamentos; }
    public void setMedicamentos(List<PedidoMedicamento> medicamentos) { this.medicamentos = medicamentos; }
}
