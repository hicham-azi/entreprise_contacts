package com.genesis.contacts.repository;


import com.genesis.contacts.domain.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
    Optional<Entreprise> findByTvaEntreprise(String tvaEntreprise);
}
