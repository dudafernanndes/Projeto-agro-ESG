package br.com.vits.orc.vits_agrochain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VITS_ORC_PAIS")
public class Country {

	@Id
	@Column(name = "vits_cod_pais")
	@Size(min = 2, max = 2)
	private String countryCode;

	@NotBlank
	@Size(max = 70)
	@Column(name = "vits_nome_pais")
	private String countryName;

	@OneToMany(mappedBy = "country")
    @Builder.Default
	private List<State> states = new ArrayList<>();
}
