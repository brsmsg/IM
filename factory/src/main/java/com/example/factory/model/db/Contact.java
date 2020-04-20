package com.example.factory.model.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dbflow5.annotation.Column;
import com.dbflow5.annotation.PrimaryKey;
import com.dbflow5.annotation.Table;
import com.dbflow5.structure.BaseModel;
import com.example.factory.utils.DiffUtils;

import java.util.Objects;

/**
 * @author brsmsg
 * @time 2020/3/29
 */
@Table(database = MyAppDB.class)
public class Contact extends BaseModel implements DiffUtils.Differ<Contact> {

    @Column
    @PrimaryKey
    private String id;
    @Column
    private String username;
    @Column
    private String faceImage;
    @Column
    private String description;

    @Column
    private String publicKey;


    public Contact() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @NonNull
    @Override
    public String toString() {
        return "id: " + id + "\n"
                +"ic_username: " + username + "\n"
                +"faceImage" + faceImage + "\n"
                +"publicKey" + publicKey + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id) &&
                Objects.equals(username, contact.username) &&
                Objects.equals(faceImage, contact.faceImage) &&
                Objects.equals(description, contact.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean isSame(Contact old) {
        return this == old ||Objects.equals(id, old.id);
    }

    @Override
    public boolean isContentSame(Contact old) {
        return this == old || Objects.equals(username, old.username) &&
                Objects.equals(faceImage, old.faceImage) &&
                Objects.equals(description, old.description);
    }

}
