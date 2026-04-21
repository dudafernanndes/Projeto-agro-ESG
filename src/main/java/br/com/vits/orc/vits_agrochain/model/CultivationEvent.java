package br.com.vits.orc.vits_agrochain.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EVENTOS_CULTIVO")
public class CultivationEvent {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    @Column(name = "id")
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lot lote;

    @NotNull
    @Column(name = "data_plantio")
    private LocalDate dataPlantio;

    @Column(name = "data_colheita_estimada")
    private LocalDate dataColheitaEstimada;

    @NotBlank
    @Column(name = "tipo_evento")
    private String tipoEvento;

    @Column(name = "descricao")
    private String descricao;

    // Compatibility accessors for older code paths
    public Lot getLot() {
        return this.lote;
    }

    public void setLot(Lot lot) {
        this.lote = lot;
    }

    public String getEventType() {
        return this.tipoEvento;
    }

    public void setEventType(String eventType) {
        this.tipoEvento = eventType;
    }

    public LocalDate getPlantingDate() {
        return this.dataPlantio;
    }

    public void setPlantingDate(LocalDate plantingDate) {
        this.dataPlantio = plantingDate;
    }

    public LocalDate getEstimatedHarvestDate() {
        return this.dataColheitaEstimada;
    }

    public void setEstimatedHarvestDate(LocalDate estimatedHarvestDate) {
        this.dataColheitaEstimada = estimatedHarvestDate;
    }

    public String getDescription() {
        return this.descricao;
    }

    public void setDescription(String description) {
        this.descricao = description;
    }

}
