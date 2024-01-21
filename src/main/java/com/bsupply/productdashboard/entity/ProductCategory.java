package com.bsupply.productdashboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;



@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "product_category")
@SQLDelete(sql = "UPDATE product_category SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction(value = "deleted=false")
@EntityListeners(AuditingEntityListener.class)
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String description;

    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "created_by", length = 50)
    @CreatedBy
    private String createdBy;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "modified_by", length = 50)
    @LastModifiedBy
    private String modifiedBy;

    private boolean deleted;

    private Instant deletedAt;
}
