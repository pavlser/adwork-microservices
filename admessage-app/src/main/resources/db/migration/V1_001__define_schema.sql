create sequence hibernate_sequence start with 1 increment by 1;

create table ad_message (
    id bigint not null AUTO_INCREMENT,
    title varchar(100) not null,
    body varchar(5000) not null,
    created_by varchar(255),
    created_date timestamp,
    last_modified_by varchar(255),
    last_modified_date timestamp,
    dislikes_count integer default 0,
    likes_count integer default 0,
    comments_count integer default 0,
    primary key (id)
);
    
create table audit_changes_entity (
    id bigint not null,
    action integer,
    date timestamp,
    user varchar(255),
    primary key (id)
);
