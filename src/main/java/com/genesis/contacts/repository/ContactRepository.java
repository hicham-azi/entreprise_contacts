package com.genesis.contacts.repository;

import com.genesis.contacts.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByFirstNameAndLastName(String firstName, String lastName);
}
