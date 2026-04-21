package br.com.vits.orc.vits_agrochain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VITS_ORC_ESTADO")
public class State {

	@Id
	@Column(name = "vits_cod_estado")
	@NotBlank
	private String stateCode;

	@NotBlank
	@Size(max = 100)
	@Column(name = "vits_nome_estado")
	private String stateName;

	@NotNull
	@ManyToOne()
	@JoinColumn(name = "vits_cod_pais", referencedColumnName = "vits_cod_pais")
	private Country country;

	@OneToMany(mappedBy = "state")
    @Builder.Default
	private List<Municipality> municipalities = new ArrayList<>();
}
