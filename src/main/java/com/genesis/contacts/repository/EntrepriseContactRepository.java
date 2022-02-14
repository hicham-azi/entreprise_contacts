package com.genesis.contacts.repository;

import com.genesis.contacts.domain.Contact;
import com.genesis.contacts.domain.EntrepriseContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntrepriseContactRepository extends JpaRepository<EntrepriseContact, Long> {
    Optional<EntrepriseContact> findByEntrepriseIdAndContactId(Long entrepriseId,Long contactId);

    boolean existsByContact(Contact contact);

    Optional<EntrepriseContact> findByContactId(Long id);
}
