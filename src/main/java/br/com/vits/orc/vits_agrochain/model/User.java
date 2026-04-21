package br.com.vits.orc.vits_agrochain.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
@Table(name = "USUARIOS_VITS")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(name = "nome_completo")
    private String name;

    @Size(max = 14)
    @Column(name = "cpf", unique = true)
    private String cpf;

    @NotBlank
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "senha_hash")
    private String password;

    @NotNull
    @Column(name = "data_cadastro")
    private LocalDateTime registrationDate;


    public EntityModel<User> toEntityModel() {
        if (this.id == null) {
            return EntityModel.of(this);
        }
        return EntityModel.of(this, Link.of("/users/" + this.id).withSelfRel());
    }

    // Compatibility getter for older code expecting getUserId()
    public Long getUserId() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Simplify roles: return a single ROLE_USER for all users
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}