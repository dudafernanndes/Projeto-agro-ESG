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
@Table(name = "VITS_ORC_TIPO_CERTIFICAÇAO")
public class CertificationType {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "tipo_certific_seq_gen"
    )
    @SequenceGenerator(
        name = "tipo_certific_seq_gen",
        sequenceName = "SEQ_TIPO_CERTIFIC",
        allocationSize = 1
    )
    @Column(name = "id_tipo_certificaçao")
    private Long certificationTypeId;

    @NotBlank
    @Size(max = 50)
    @Column(name = "vits_nome_certificaçao")
    private String certificationName;
}
