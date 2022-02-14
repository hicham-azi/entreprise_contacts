package com.genesis.contacts.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Contact extends AuditableEntity{
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String address;

//Add validation @notNull with condition
private String tvaContact;

@OneToMany(mappedBy = "contact")
private List<EntrepriseContact> entrepriseContacts;
}
