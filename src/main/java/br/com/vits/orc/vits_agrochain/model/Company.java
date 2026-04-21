package br.com.vits.orc.vits_agrochain.model;

import java.time.LocalDateTime;

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
@Table(name = "VITS_ORC_EMPRESA")
public class Company {

    @Id
    @Size(max = 18)
    @Column(name = "vits_cnpj_empres")
    private String companyCnpj;

    @NotNull
    @Column(name = "vits_data_cad_empres")
    private LocalDateTime registrationDate;

    @NotBlank
    @Size(max = 200)
    @Column(name = "vits_nome_empres")
    private String companyName;

    @NotBlank
    @Size(max = 200)
    @Column(name = "vits_gestor_empresa")
    private String companyManager;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "vits_id_categ_empresa")
    private CompanyCategory companyCategory;
}
