package com.monitorfree.UserModel;

import android.databinding.BaseObservable;
import android.util.Log;

/**
 * Created by jaspreet on 11/10/17.
 */

public class User extends BaseObservable {
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    String password;
    String fullName;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    String key;
    String sig;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String hash;
    String id;

    String externalUserId;
    String externalPhotoUrl;
    String accountType;

    public String getExternalUserId() {
        return externalUserId;
    }

    public void setExternalUserId(String externalUserId) {
        this.externalUserId = externalUserId;
    }

    public String getExternalPhotoUrl() {
        return externalPhotoUrl;
    }

    public void setExternalPhotoUrl(String externalPhotoUrl) {
        this.externalPhotoUrl = externalPhotoUrl;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
