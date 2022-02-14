create table entreprise
(
    id                        int8        not null,
    created                   timestamp,
    updated                   timestamp,
    version                   int8,
    address             varchar(255),
    tva_entreprise                varchar(255),
    primary key (id)
);

create table contact
(
    id            int8 not null,
    created       timestamp,
    updated       timestamp,
    version       int8,
    first_name   varchar(255),
    last_name   varchar(255),
    address     varchar(255),
    tva_contact   varchar(255),
    primary key (id)
);

create table entreprise_contact
(
    id                int8    not null,
    created           timestamp,
    updated           timestamp,
    version           int8,
    type_contact   varchar(60),
    entreprise_id    int8    not null,
    contact_id int8    not null,
    primary key (id)
);


alter table entreprise_contact
    add constraint FK2jieaq7wjd1eeufsnvnyjvw9e
        foreign key (entreprise_id)
            references entreprise;

alter table entreprise_contact
    add constraint FK2hk1annvn57cv42r8dhqk72wk
        foreign key (contact_id)
            references contact;
