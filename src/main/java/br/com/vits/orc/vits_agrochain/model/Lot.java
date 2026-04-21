package br.com.vits.orc.vits_agrochain.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LOTES")
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "nome_lote")
    private String nomeLote;

    @NotBlank
    @Size(max = 100)
    @Column(name = "cultura")
    private String cultura;

    @NotNull
    @Column(name = "producao_total")
    private java.math.BigDecimal producaoTotal;

    @NotNull
    @Column(name = "custo_total")
    private java.math.BigDecimal custoTotal;

    @NotNull
    @Column(name = "preco_venda")
    private java.math.BigDecimal precoVenda;

    @Column(name = "status")
    private String status;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    // Compatibility getters expected by existing services
    public Double getTotalProduction() {
        return this.producaoTotal == null ? null : this.producaoTotal.doubleValue();
    }

    public void setTotalProduction(Double totalProduction) {
        this.producaoTotal = totalProduction == null ? null : java.math.BigDecimal.valueOf(totalProduction);
    }

    public Double getTotalCost() {
        return this.custoTotal == null ? null : this.custoTotal.doubleValue();
    }

    public void setTotalCost(Double totalCost) {
        this.custoTotal = totalCost == null ? null : java.math.BigDecimal.valueOf(totalCost);
    }

    public Double getSalePrice() {
        return this.precoVenda == null ? null : this.precoVenda.doubleValue();
    }

    public void setSalePrice(Double salePrice) {
        this.precoVenda = salePrice == null ? null : java.math.BigDecimal.valueOf(salePrice);
    }

    public Long getLotId() {
        return this.id;
    }

    public Integer getLotNumber() {
        return this.id == null ? null : this.id.intValue();
    }

    public Integer getLotStatus() {
        if (this.status == null) return null;
        String normalized = this.status.trim().toLowerCase();
        return normalized.equals("finalizado") ? 1 : 0;
    }

    public void setLotStatus(Integer lotStatus) {
        if (lotStatus == null) {
            this.status = null;
            return;
        }
        this.status = lotStatus == 1 ? "finalizado" : "ativo";
    }

    public Culture getCulture() {
        if (this.cultura == null) return null;
        return new Culture(null, this.cultura, null, null);
    }
}
