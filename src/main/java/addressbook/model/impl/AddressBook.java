package addressbook.model.impl;


import addressbook.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "AddressBook")
public class AddressBook extends BaseModel {

    private String name;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "addressBookId")
    private Set<Contact> contacts;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
}
