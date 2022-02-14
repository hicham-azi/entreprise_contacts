package com.genesis.contacts.serviceImpl;

import com.genesis.contacts.domain.Contact;
import com.genesis.contacts.domain.EntrepriseContact;
import com.genesis.contacts.domain.enums.ContactTypeE;
import com.genesis.contacts.domain.enums.ERROR_CODE;
import com.genesis.contacts.exceptions.ValidationException;
import com.genesis.contacts.repository.ContactRepository;
import com.genesis.contacts.repository.EntrepriseContactRepository;
import com.genesis.contacts.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    EntrepriseContactRepository entrepriseContactRepository;

    @Override
    public Page<Contact> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    @Override
    public void save(Contact contact) throws ValidationException {
        Optional<Contact> optionalContact = contactRepository.findByFirstNameAndLastName
                (contact.getFirstName(), contact.getLastName());
        if (optionalContact.isPresent()) {
            throw new ValidationException(ERROR_CODE.DUPLICATE_VALUE);
        } else {
            contactRepository.save(contact);
        }
    }

    @Override
    public void edit(Contact contact) throws ValidationException {
        Optional<Contact> optionalContact = contactRepository.findById(contact.getId());
        if (optionalContact.isPresent()) {
             if (contact.getEntrepriseContacts() != null && !contact.getEntrepriseContacts().isEmpty()) {
                boolean existSFreelance = contact.getEntrepriseContacts().stream().filter(
                        a -> a.getContactType().equals(ContactTypeE.FREELANCE.name())).
                        findAny().
                        isPresent();

                if (existSFreelance &&
                        (contact.getTvaContact() == null || contact.getTvaContact().isBlank())) {
                    throw new ValidationException(ERROR_CODE.TVA_CONTACT_MONDATORY);
                }
            }

            Contact contactToUpdate = optionalContact.get();
            contactToUpdate.setFirstName(contact.getFirstName());
            contactToUpdate.setLastName(contact.getLastName());
            contactToUpdate.setAddress(contact.getAddress());
            contactToUpdate.setTvaContact(contact.getTvaContact());
            contactRepository.save(contactToUpdate);
        } else {
            throw new ValidationException(ERROR_CODE.INEXISTANT_CONTACT);
        }
    }

    @Override
    public void remove(Long contactId) throws ValidationException {
        Optional<Contact> optionalContact = contactRepository.findById(contactId);
        if (optionalContact.isPresent()) {
            if (!entrepriseContactRepository.existsByContact(optionalContact.get())) {
                Contact contactToDelete = optionalContact.get();
                contactRepository.delete(contactToDelete);
            } else {
                throw new ValidationException(ERROR_CODE.CONTACT_USED);
            }
        } else {
            throw new ValidationException(ERROR_CODE.INEXISTANT_CONTACT);
        }
    }
}
