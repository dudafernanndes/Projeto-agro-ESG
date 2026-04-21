package br.com.vits.orc.vits_agrochain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VITS_ORC_CATEGORIA_CULTIVO")
public class CultureCategory {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "categ_cultivo_seq_gen"
    )
    @SequenceGenerator(
        name = "categ_cultivo_seq_gen",
        sequenceName = "SEQ_CATEG_CULTIVO",
        allocationSize = 1
    )
    @Column(name = "vits_id_categ_cultivo")
    private Long cultureCategoryId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "vits_descricao_cultivo")
    private String description;
}
