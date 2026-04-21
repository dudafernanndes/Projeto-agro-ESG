package br.com.vits.orc.vits_agrochain.model;

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
@Table(name = "VITS_ORC_CULTURA")
public class Culture {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cultura_seq_gen"
    )
    @SequenceGenerator(
        name = "cultura_seq_gen",
        sequenceName = "SEQ_CULTURA",
        allocationSize = 1
    )
    @Column(name = "vits_id_cultura")
    private Long cultureId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "vits_nome_cultivo")
    private String cultureName;

    @Size(max = 200)
    @Column(name = "vits_desc")
    private String description;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vits_id_categ_cultivo")
    private CultureCategory cultureCategory;

}
