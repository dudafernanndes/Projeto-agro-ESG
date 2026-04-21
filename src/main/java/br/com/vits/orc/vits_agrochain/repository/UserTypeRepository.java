package br.com.vits.orc.vits_agrochain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.vits.orc.vits_agrochain.model.UserType;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    Optional<UserType> findByUserDescription(String description);
}

