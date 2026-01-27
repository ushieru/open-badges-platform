package com.gdgguadalajara.issuer.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonRawValue;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Issuer extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public UUID id;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String description;

    @Column(nullable = false)
    public String url;

    @Column(nullable = false)
    public String email;

    @Column(nullable = false)
    public String logoUrl;

    @Lob
    @JsonRawValue
    public String jsonPayload;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public Instant createdAt;
}
