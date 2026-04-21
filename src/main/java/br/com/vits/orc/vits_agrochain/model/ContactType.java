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
@Table(name = "VITS_ORC_TIPOS_CONTATO")
public class ContactType {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "tipo_contato_seq_gen"
    )
    @SequenceGenerator(
        name = "tipo_contato_seq_gen",
        sequenceName = "SEQ_TIPO_CONTATO",
        allocationSize = 1
    )
    @Column(name = "vits_id_tipos_contato")
    private Long contactTypeId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "vits_tipos_contato")
    private String contactTypeName;
}
