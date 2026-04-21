package br.com.vits.orc.vits_agrochain.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;

import br.com.vits.orc.vits_agrochain.controller.PropertyController;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VITS_ORC_PROPRIEDADE")
public class Property {

    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "propriedade_seq_gen"
    )
    @SequenceGenerator(
        name = "propriedade_seq_gen",
        sequenceName = "SEQ_PROPRIEDADE",
        allocationSize = 1
    )
    @Column(name = "vits_id_prop")
    private Long propertyId;

    @NotBlank
    @Size(max = 70)
    @Column(name = "vits_nome_prop")
    private String propertyName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "vits_area_total_prop")
    private String totalArea;

    @NotBlank
    @Size(max = 8)
    @Column(name = "vits_cep_prop")
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "vits_id_usuario")
    private User user;

    public EntityModel<Property> toEntityModel() {
        return EntityModel.of(this)
                .add(linkTo(methodOn(PropertyController.class)
                        .getById(this.propertyId)).withSelfRel());
    }
}
