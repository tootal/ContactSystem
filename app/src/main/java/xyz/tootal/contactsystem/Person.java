package xyz.tootal.contactsystem;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class Person extends DataSupport implements Serializable {
    private int id;
    private String name;
    private String number;
    private String imageUri;
    private String note;

    public String getNote() {
        return note;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person(){

    }

    public Person(String name,String number) {
        setName(name);
        setNumber(number);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }


}