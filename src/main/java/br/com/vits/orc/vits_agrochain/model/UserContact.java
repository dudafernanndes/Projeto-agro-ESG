package br.com.vits.orc.vits_agrochain.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VITS_ORC_CONTATOS_USUARIO")
public class UserContact {

    @Id
    @Column(name = "vits_id_contatos_usuario")
    private Long userContactId;

    @Id
    @Column(name = "vits_id_usuario")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "vits_id_usuario", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "vits_id_tipos_contato")
    private ContactType contactType;
}
