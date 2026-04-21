package br.com.vits.orc.vits_agrochain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VITS_ORC_TIPO_USUARIO")
public class UserType {

    @Id
    @GeneratedValue(                            
        strategy = GenerationType.SEQUENCE,
        generator = "tipo_usuario_seq_gen"
    )
    @SequenceGenerator(
        name = "tipo_usuario_seq_gen", 
        sequenceName = "SEQ_TIPO_USUARIO", 
        allocationSize = 1
    )
    @Column(name = "id_tipo_usuario")
    private Long userTypeId;

    @NotBlank
    @Column(name = "vits_usuario_descrição")
    private String userDescription;
}