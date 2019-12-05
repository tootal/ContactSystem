package xyz.tootal.contactsystem;

import java.io.Serializable;

class Person implements Serializable {
    private int id;
    private String name;
    private String number;

    public Person(String name, String number){
        setName(name);
        setNumber(number);
    }

    public Person(int id,String name, String number){
        setId(id);
        setName(name);
        setNumber(number);
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

    private void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    private void setNumber(String number) {
        this.number = number;
    }
}
