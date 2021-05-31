package addressbook.service.addressbook;

import addressbook.model.impl.AddressBook;
import addressbook.model.impl.Contact;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * Class implement this interface contains logic of perform CRUD operation against underlining DataStore
 *
 */
public interface AddressBookService {

    /**
     * Return {@link AddressBook} with matching id.
     *
     * @param id
     * @return
     */
    Optional<AddressBook> get (UUID id);

    /**
     * Return All {@link AddressBook}
     *
     * @return
     */
    List<AddressBook> list();

    /**
     * Create a new addressbook
     *
     * @param addressBook
     */
    void create(AddressBook addressBook);


    void create(Contact contact);

    /**
     * Update an exist AddressBook with given ID
     *
     * @param id
     * @param addressBook
     */
    void update(UUID id, AddressBook addressBook);

    /**
     * Return Contact with matching Id
     *
     * @param id
     * @return
     */
    Optional<Contact> getContact(UUID id);

    /**
     * Return all available contact in unique dataset
     *
     * @return
     */
    Set<Contact> getAllUniqueContact();


    /**
     * Update Contact with matching Id, using given Pojo data
     *
     * @param id
     * @param contact
     */
    void update(UUID id, Contact contact);


    void delete (UUID id);

    void deleteContact (UUID id);


}
