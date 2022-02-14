package com.genesis.contacts.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Entreprise extends AuditableEntity{

  private String address;
  private String tvaEntreprise;

  @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
  private List<EntrepriseContact> entreprisesContacts;
}
