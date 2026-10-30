package com.example.app.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "user_address",
    indexes = @Index(name = "idx_user_address_user_id", columnList = "user_id"))
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "address_label", length = 50)
    private String addressLabel;
    @Column(name = "recipient_name", length = 150)
    private String recipientName;
    @Column(name = "contact_phone", length = 32)
    private String contactPhone;
    @Column(name = "address_line1", nullable = false, length = 255)
    private String addressLine1;
    @Column(name = "address_line2", length = 255)
    private String addressLine2;
    @Column(length = 255)
    private String landmark;
    @Column(length = 150)
    private String locality;
    @Column(length = 150)
    private String city;
    @Column(length = 150)
    private String district;
    @Column(name = "state_region", length = 150)
    private String stateRegion;
    @Column(name = "postal_code", length = 20)
    private String postalCode;
    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

//    @JdbcTypeCode(SqlTypes.GEOGRAPHY)
//    @Column(columnDefinition = "geography(Point,4326)")
//    private Point location;

    @Column(name = "is_default_shipping", nullable = false)
    private boolean defaultShipping;
    @Column(name = "is_default_billing", nullable = false)
    private boolean defaultBilling;
    @Column(name = "delivery_instructions", columnDefinition = "text")
    private String deliveryInstructions;
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    protected UserAddress() {}
    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }
    @PreUpdate
    protected void onUpdate() { updatedAt = OffsetDateTime.now(ZoneOffset.UTC); }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getAddressLabel() { return addressLabel; }
    public void setAddressLabel(String value) { this.addressLabel = value; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String value) { this.recipientName = value; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String value) { this.contactPhone = value; }
    public String getAddressLine1() { return addressLine1; }
    public void setAddressLine1(String value) { this.addressLine1 = value; }
    public String getAddressLine2() { return addressLine2; }
    public void setAddressLine2(String value) { this.addressLine2 = value; }
    public String getLandmark() { return landmark; }
    public void setLandmark(String value) { this.landmark = value; }
    public String getLocality() { return locality; }
    public void setLocality(String value) { this.locality = value; }
    public String getCity() { return city; }
    public void setCity(String value) { this.city = value; }
    public String getDistrict() { return district; }
    public void setDistrict(String value) { this.district = value; }
    public String getStateRegion() { return stateRegion; }
    public void setStateRegion(String value) { this.stateRegion = value; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String value) { this.postalCode = value; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String value) { this.countryCode = value; }
//    public Point getLocation() { return location; }
//    public void setLocation(Point value) { this.location = value; }
    public boolean isDefaultShipping() { return defaultShipping; }
    public void setDefaultShipping(boolean value) { this.defaultShipping = value; }
    public boolean isDefaultBilling() { return defaultBilling; }
    public void setDefaultBilling(boolean value) { this.defaultBilling = value; }
    public String getDeliveryInstructions() { return deliveryInstructions; }
    public void setDeliveryInstructions(String value) { this.deliveryInstructions = value; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public OffsetDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(OffsetDateTime value) { this.deletedAt = value; }
}
