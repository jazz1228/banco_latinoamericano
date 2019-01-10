package com.udea.web.banco.banco.ModelMONGO;


import com.udea.web.banco.banco.ModelMONGO.Account;
import com.udea.web.banco.banco.ModelMONGO.Country;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document(collection = "User")
public class User implements Serializable {
    @Id
    private Object iden;
    private String id;
    private String name;
    private String pass;
    private String birthDay;
    private String phone;
    private String address;
    private Country country;
    private Account numberAccount;
    private String email;
    private String role;

    public Object getIden() {
        return iden;
    }

    public void setIden(Object iden) {
        this.iden = iden;
    }

    public Account getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(Account numberAccount) {
        this.numberAccount = numberAccount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}