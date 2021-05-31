package addressbook.controller;


import addressbook.model.impl.AddressBook;
import addressbook.model.impl.Contact;
import addressbook.service.addressbook.AddressBookService;
import addressbook.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Contain all Address book API
 *
 *
 */
@RestController
@RequestMapping("/api/addressbook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;


    public AddressBookController (AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }


    /**
     * Return all Address Book
     *
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity<List<AddressBook>> listAllAddressBook () {
        return ResponseEntity.ok(addressBookService.list());
    }

    /**
     * Find the AddressBook with given Id. Respond {@link org.springframework.http.HttpStatus#NOT_FOUND} if the required
     * AddressBook is not found.
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<AddressBook> getAddressBook (@PathVariable("id") UUID id) {
        Optional<AddressBook> addressBook = addressBookService.get(id);

        ResponseEntity<AddressBook> response;

        if(addressBook.isPresent()){
            response = ResponseEntity.ok(addressBook.get());
        } else {
            response = ResponseEntity.notFound().build();
        }

        return response;
    }

    /**
     * Create a new {@link AddressBook}
     *
     * @param addressBook
     * @return
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<UUID> createAddressBook(@RequestBody AddressBook addressBook) {
        addressBookService.create (addressBook);
        return ResponseEntity.ok(addressBook.getId());
    }


    /**
     *
     * Update an AddressBook
     *
     * @param addressBook
     * @param id
     * @return
     */
    @PutMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity updateAddressBook (@RequestBody AddressBook addressBook, @PathVariable("id") UUID id) {
        addressBookService.update (id, addressBook);
        return ResponseEntity.ok().build();
    }

    /**
     * Return all Unique Contacts
     *
     *
     * @return
     */
    @GetMapping("/contact")
    public ResponseEntity<Set<Contact>> getAllContacts () {
        return ResponseEntity.ok(addressBookService.getAllUniqueContact());
    }

    /**
     * Find the AddressBook with given Id. Respond {@link org.springframework.http.HttpStatus#NOT_FOUND} if the required
     * AddressBook is not found.
     *
     * @param id
     * @return
     */
    @GetMapping("/contact/{id}")
    public ResponseEntity<Contact> get (@PathVariable("id") UUID id) {
        Optional<Contact> contact = addressBookService.getContact(id);
        return contact.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Return all Contacts under a given AddressBook
     *
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/contact")
    public ResponseEntity<Set<Contact>> getContacts (@PathVariable("id") UUID id) {
        Optional<AddressBook> addressBook = addressBookService.get(id);
        return addressBook.map(book -> ResponseEntity.ok(book.getContacts()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create new Contact under a given {@link AddressBook}
     *
     * @param contact
     * @param addressBookId
     * @return
     */
    @PostMapping(path="/{id}/contact", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UUID> create(@RequestBody Contact contact, @PathVariable("id") UUID addressBookId ) {
        Optional<AddressBook> addressBook = addressBookService.get(addressBookId);

        if (!addressBook.isPresent()) {
            throw new NotFoundException(String.format("Cannot find address book with id [%s]", addressBookId));
        }

        contact.setAddressBook(addressBook.get());
        addressBookService.create (contact);
        return ResponseEntity.ok(contact.getId());
    }


    /**
     * Update new Contact under a given {@link AddressBook}, and move the {@link Contact} to given {@link AddressBook}
     *
     * @param contact
     * @param addressBookId
     * @return
     */
    @PutMapping(path="/{addressBookId}/contact/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UUID> update(@RequestBody Contact contact, @PathVariable("addressBookId") UUID addressBookId, @PathVariable("id") UUID id ) {
        Optional<AddressBook> addressBook = addressBookService.get(addressBookId);

        if (!addressBook.isPresent()) {
            throw new NotFoundException(String.format("Cannot find address book with id [%s]", addressBookId));
        }

        contact.setAddressBook(addressBook.get());
        addressBookService.update (id, contact);
        return ResponseEntity.ok(contact.getId());
    }


    /**
     * Delete a {@link AddressBook} with given Id
     *
     * @param id
     * @return
     */
    @DeleteMapping (path = "/{id}")
    public ResponseEntity delete (@PathVariable("id") UUID id) {
        addressBookService.delete (id);
        return ResponseEntity.ok().build();
    }


    /**
     * Delete a {@link AddressBook} with given Id
     *
     * @param id
     * @return
     */
    @DeleteMapping (path = "/contact/{id}")
    public ResponseEntity deleteContact (@PathVariable("id") UUID id) {
        addressBookService.deleteContact (id);
        return ResponseEntity.ok().build();
    }

}
