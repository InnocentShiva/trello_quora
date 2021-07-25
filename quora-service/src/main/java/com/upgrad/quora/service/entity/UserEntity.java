package com.upgrad.quora.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "users", schema = "public" )
@NamedQueries(
        {
                @NamedQuery(name = "userByUsername", query = "select u from UserEntity u where u.username = :username"),
                @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email = :email"),
                @NamedQuery(name = "userByUserUUID", query = "select u from UserEntity u where u.uuid = :uuid")
        }
)

public class UserEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;


    @Column(name= "ROLE")
    @Size(max = 30)
    private String role;

    @Column(name = "EMAIL")
    @NotNull
    @Size(max = 50)
    private String email;


    @Column(name = "PASSWORD")
    @NotNull
    @Size(max = 255)
    private String password;


    @Column(name = "FIRSTNAME")
    @NotNull
    @Size(max = 30)
    private String firstName;

    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 30)
    private String lastName;


    @Column(name = "USERNAME")
    @NotNull
    @Size(max = 30)
    private String username;

    @Column(name = "CONTACTNUMBER")
    @NotNull
    @Size(max = 30)
    private String contact_number;

    @Column(name = "DOB")
    @NotNull
    @Size(max = 30)
    private String dob;

    @Column(name = "ABOUTME")
    @NotNull
    @Size(max = 30)
    private String about_me;


    @Column(name = "COUNTRY")
    @NotNull
    @Size(max = 50)
    private String country;


    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    private String salt;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getDob() {
        return this.dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id) && uuid.equals(that.uuid) && role.equals(that.role) && email.equals(that.email) && password.equals(that.password) && firstName.equals(that.firstName) && lastName.equals(that.lastName) && username.equals(that.username) && contact_number.equals(that.contact_number) && dob.equals(that.dob) && about_me.equals(that.about_me) && country.equals(that.country) && salt.equals(that.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uuid, role, email, password, firstName, lastName, username, contact_number, dob, about_me, country, salt);
    }
}
