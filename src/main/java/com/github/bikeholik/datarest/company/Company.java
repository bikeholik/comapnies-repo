package com.github.bikeholik.datarest.company;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private String address;

    @Column(nullable = false)
    @NotNull
    private String city;

    @Column(nullable = false)
    @NotNull
    private String country;

    @Column(nullable = true)
    @Email
    private String email;

    @Column(nullable = true)
    private String phoneNumber;

    @ManyToMany
    @JoinTable(
            name = "COMPANY_OWNERS",
            joinColumns = @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "OWNER_ID", referencedColumnName = "ID"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"COMPANY_ID", "OWNER_ID"}))
    private List<Owner> owners;

    private Company(String name, String address, String city, String country, String email, String phoneNumber, List<Owner> owners) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.owners = owners;
    }

    Company() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public static class CompanyBuilder {
        private String name;
        private String address;
        private String city;
        private String country;
        private String email;
        private String phoneNumber;
        private List<Owner> owners;

        public CompanyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CompanyBuilder address(String address) {
            this.address = address;
            return this;
        }

        public CompanyBuilder city(String city) {
            this.city = city;
            return this;
        }

        public CompanyBuilder country(String country) {
            this.country = country;
            return this;
        }

        public CompanyBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CompanyBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public CompanyBuilder owners(List<Owner> owners) {
            this.owners = owners;
            return this;
        }

        public Company build() {
            return new Company(name, address, city, country, email, phoneNumber, owners);
        }
    }
}
