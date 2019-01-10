package com.udea.web.banco.banco.ModelMONGO;
import com.udea.web.banco.banco.ModelMONGO.Account;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document(collection = "Transaction")
public class Transaction implements Serializable {


    @Id
    private long id;
    private String account;
    private Account finalAccount;
    private String date;
    private String type;
    private double amount;
    private String coin;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Account getFinalAccount() {
        return finalAccount;
    }

    public void setFinalAccount(Account finalAccount) {
        this.finalAccount = finalAccount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }
}



