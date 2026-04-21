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
@Table(name = "VITS_ORC_ENDEREÇO")
public class Address {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "endereco_seq_gen"
    )
    @SequenceGenerator(
        name = "endereco_seq_gen",
        sequenceName = "SEQ_ENDERECO",
        allocationSize = 1
    )
    @Column(name = "vits_id_endereço")
    private Long addressId;

    @NotBlank
    @Size(max = 200)
    @Column(name = "vits_nome_rua")
    private String streetName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "vits_nome_bairro")
    private String neighborhood;

    @NotBlank
    @Size(max = 8)
    @Column(name = "vits_cep")
    private String zipCode;

    @NotBlank
    @Size(max = 5)
    @Column(name = "vits_numero_local")
    private String number;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vits_id_prop")
    private Property property;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vits_cod_municipio")
    private Municipality municipality;
}
