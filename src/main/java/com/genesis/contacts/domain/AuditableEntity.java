package com.genesis.contacts.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.genesis.contacts.utils.Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

@Slf4j
@Data
@MappedSuperclass
//@EntityListeners(SelaEntityListener.class)
@EqualsAndHashCode(callSuper = true)
public abstract class AuditableEntity extends CommonEntity {

    @JsonIgnore
    @Transient
    private String previousState;

    @PostLoad
    private void storeState() {

        try {
            previousState = Utils.convertObjectToJson(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
