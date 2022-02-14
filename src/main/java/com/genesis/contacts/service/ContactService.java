package com.genesis.contacts.service;


import com.genesis.contacts.domain.Contact;
import com.genesis.contacts.exceptions.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface ContactService {
    Page<Contact> findAll(Pageable pageable);

    void save(Contact contact) throws ValidationException;

    void edit(Contact contact) throws ValidationException;

    void remove(Long contactId) throws ValidationException;
}
