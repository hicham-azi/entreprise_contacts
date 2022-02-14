package com.genesis.contacts.serviceImpl;

import com.genesis.contacts.domain.Contact;
import com.genesis.contacts.domain.Entreprise;
import com.genesis.contacts.domain.EntrepriseContact;
import com.genesis.contacts.domain.enums.ContactTypeE;
import com.genesis.contacts.domain.enums.ERROR_CODE;
import com.genesis.contacts.exceptions.ValidationException;
import com.genesis.contacts.repository.ContactRepository;
import com.genesis.contacts.repository.EntrepriseContactRepository;
import com.genesis.contacts.repository.EntrepriseRepository;
import com.genesis.contacts.service.EntrepriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class EntrepriseServiceImpl implements EntrepriseService {

    @Autowired
    EntrepriseRepository entrepriseRepository;

    @Autowired
    EntrepriseContactRepository entrepriseContactRepository;

    @Autowired
    ContactRepository contactRepository;

    @Override
    public Page<Entreprise> findAll(Pageable pageable) {
        return entrepriseRepository.findAll(pageable);
    }

    @Override
    public Entreprise findByTvaEntreprise(String tvaEntreprise) {
        Optional<Entreprise> optionalEntrepriseContact = entrepriseRepository.findByTvaEntreprise(tvaEntreprise);
        return optionalEntrepriseContact.isPresent() ? optionalEntrepriseContact.get() : null;
    }

    @Override
    public void save(Entreprise entreprise) throws ValidationException {
        Optional<Entreprise> optionalEntreprise = entrepriseRepository.findByTvaEntreprise(entreprise.getTvaEntreprise());
        if (optionalEntreprise.isPresent()) {
            throw new ValidationException(ERROR_CODE.DUPLICATE_VALUE);
        } else {
            entrepriseRepository.save(entreprise);
        }
    }

    @Override
    public void edit(Entreprise entreprise) throws ValidationException {
        Optional<Entreprise> optionalEntreprise = entrepriseRepository.findById(entreprise.getId());
        if (optionalEntreprise.isPresent()) {
            Entreprise entrepriseToUpdate = optionalEntreprise.get();
            entrepriseToUpdate.setAddress(entreprise.getAddress());
        } else {
            throw new ValidationException(ERROR_CODE.INEXISTANTE_ENTREPRISE);
        }
    }

    @Override
    public void addContact(Long entrepriseId, Long contactId, String contactType) throws ValidationException {
        Optional<EntrepriseContact> entrepriseContactOptional =
                entrepriseContactRepository.findByEntrepriseIdAndContactId(entrepriseId, contactId);
        if (entrepriseContactOptional.isPresent()) {
            throw new ValidationException(ERROR_CODE.DUPLICATE_VALUE);
        } else {
            Optional<Entreprise> optionalEntreprise = entrepriseRepository.findById(entrepriseId);
            Optional<Contact> optionalContact = contactRepository.findById(contactId);
            if (optionalEntreprise.isPresent() && optionalContact.isPresent()) {
                if (contactType.equals(ContactTypeE.FREELANCE.name()) &&
                        (optionalContact.get().getTvaContact() == null || optionalContact.get().getTvaContact().isEmpty())) {
                    throw new ValidationException(ERROR_CODE.TVA_CONTACT_MONDATORY);
                }
                EntrepriseContact entrepriseContact = new EntrepriseContact();
                entrepriseContact.setEntreprise(optionalEntreprise.get());
                entrepriseContact.setContact(optionalContact.get());

                entrepriseContact.setContactType(contactType);
                entrepriseContactRepository.save(entrepriseContact);
            } else {
                throw new ValidationException(ERROR_CODE.OBJ_NOT_FOUND);
            }
        }
    }
}
