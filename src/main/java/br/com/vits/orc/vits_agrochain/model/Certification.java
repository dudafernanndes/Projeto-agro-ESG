package br.com.vits.orc.vits_agrochain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VITS_ORC_CERTIFICACOES")
public class Certification {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "certificacao_seq_gen"
    )
    @SequenceGenerator(
        name = "certificacao_seq_gen",
        sequenceName = "SEQ_CERTIFICACAO",
        allocationSize = 1
    )
    @Column(name = "vits_cod_certific")
    private Long certificationCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_tipo_certificaçao")
    private CertificationType certificationType;
}
