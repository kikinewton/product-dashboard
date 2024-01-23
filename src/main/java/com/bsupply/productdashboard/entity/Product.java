package com.bsupply.productdashboard.entity;

import com.bsupply.productdashboard.enums.MeasurementUnits;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@SQLRestriction(value = "deleted=false")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String description;

    @Positive(message = "Quantity per pack must be positive")
    private int quantityPerPack;

    @Enumerated(EnumType.STRING)
    private MeasurementUnits measurementUnits;

    @Positive(message = "Weight must be positive")
    private double weight;

    @PositiveOrZero
    private double packWeightInKg;

    @ManyToOne
    @JoinColumn(name = "product_category_id", referencedColumnName = "id")
    private ProductCategory productCategory;

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

    @PrePersist
    private void calculateWeightPerPack() {
        if (MeasurementUnits.GRAM.equals(measurementUnits)) {
            packWeightInKg = quantityPerPack * (weight / 1000);
        } else packWeightInKg = quantityPerPack * weight;
    }
}
