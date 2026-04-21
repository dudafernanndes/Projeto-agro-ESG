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
@Table(name = "VITS_ORC_CATEGORIA_EMPRESA")
public class CompanyCategory {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "categ_empresa_seq_gen"
    )
    @SequenceGenerator(
        name = "categ_empresa_seq_gen",
        sequenceName = "SEQ_CATEG_EMPRESA",
        allocationSize = 1
    )
    @Column(name = "vits_id_categ_empresa")
    private Long companyCategoryId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "vits_categorias_descricao")
    private String description;
}
