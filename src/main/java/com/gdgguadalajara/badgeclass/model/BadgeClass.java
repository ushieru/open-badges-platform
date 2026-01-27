package com.gdgguadalajara.badgeclass.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.gdgguadalajara.issuer.model.Issuer;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BadgeClass extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issuer_id", nullable = false)
    public Issuer issuer;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String description;

    @Column(nullable = false)
    public String imageUrl;

    @Column(columnDefinition = "TEXT")
    public String criteriaMd;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String jsonPayload;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    public Instant createdAt;
}
