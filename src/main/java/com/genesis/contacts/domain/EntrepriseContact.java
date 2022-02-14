package com.genesis.contacts.domain;

import com.genesis.contacts.domain.enums.ContactTypeE;
import com.genesis.contacts.utils.ValidEnumeration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EntrepriseContact extends AuditableEntity{

    @ManyToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "contact_id")
    private Contact contact;

    @ManyToOne(fetch = FetchType.LAZY, optional = false,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    @ValidEnumeration(targetClassType = ContactTypeE.class)
    private String contactType;
}
