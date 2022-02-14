package com.genesis.contacts.service;

import com.genesis.contacts.domain.Contact;
import com.genesis.contacts.domain.Entreprise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.ValidationException;

public interface EntrepriseService {
    Page<Entreprise> findAll(Pageable pageable);

    Entreprise findByTvaEntreprise(String tvaEntreprise);

    void save(Entreprise entreprise) throws  com.genesis.contacts.exceptions.ValidationException;

    void edit(Entreprise entreprise) throws ValidationException, com.genesis.contacts.exceptions.ValidationException;

    void addContact(Long entrepriseId, Long contactId , String tvaEntreprise) throws com.genesis.contacts.exceptions.ValidationException;
}
