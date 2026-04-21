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
@Table(name = "VITS_ORC_MUNICIPIO")
public class Municipality {

	@Id
	@Column(name = "vits_cod_municipio")
	private int municipalityCode;

	@NotBlank
	@Size(max = 200)
	@Column(name = "vits_nome_municipio")
	private String municipalityName;

	@NotNull
	@ManyToOne()
	@JoinColumn(name = "vits_cod_estado", referencedColumnName = "vits_cod_estado")
	private State state;
}
