package addressbook.service.addressbook.jpaimpl;

import addressbook.dao.AddressBookRepository;
import addressbook.dao.ContactRepository;
import addressbook.model.impl.AddressBook;
import addressbook.model.impl.Contact;
import addressbook.service.addressbook.AddressBookService;
import addressbook.service.addressbook.contactvalidator.ContactValidator;
import addressbook.service.addressbook.contactvalidator.ContactValidatorResult;
import addressbook.service.exception.NotFoundException;
import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


/**
 * This class access AddressBook and Contact in DataBase with JPA.
 *
 */
@Service
public class JpaAddressBookService implements AddressBookService {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactValidator contactValidator;


    public JpaAddressBookService(AddressBookRepository addressBookRepository, ContactRepository contactRepository) {
        this.addressBookRepository = addressBookRepository;
        this.contactRepository = contactRepository;
    }

    @Override
    public Optional<AddressBook> get (UUID id) {
        return addressBookRepository.findById(id);
    }

    @Override
    public Optional<Contact> getContact(UUID id) {
        return contactRepository.findById(id);
    }


    /**
     * Return all unique contact
     *
     * @return
     */
    @Override
    public Set<Contact> getAllUniqueContact() {
        return Sets.newHashSet(contactRepository.findAll());

    }


    /**
     * Return all AddressBook
     *
     * @return
     */
    @Override
    public List<AddressBook> list() {
        return addressBookRepository.findAll();
    }

    /**
     * Create a new address book in DB
     *
     *
     * @param addressBook
     */
    @Override
    public void create(AddressBook addressBook) {
        addressBookRepository.save(addressBook);
    }


    /**
     * Update an Existing AddressBook. {@link NotFoundException} will be thrown if AddressBook cannot be found with
     * given Id
     *
     * @param id
     * @param addressBook
     */
    @Override
    public void update(UUID id, AddressBook addressBook) {
        Optional<AddressBook> existAddressBook = addressBookRepository.findById(id);
        if(existAddressBook.isPresent()) {
            AddressBook addressBookEntity = existAddressBook.get();
            BeanUtils.copyProperties(addressBook, addressBookEntity, "id", "contacts");
            addressBookRepository.save(addressBookEntity);
        } else {
            throw new NotFoundException(String.format("AddressBook cannot be found with given Id [%s]", id.toString()));
        }
    }

    /**
     * Save an Contact. Validate before saving
     *
     * @param contact
     */
    @Override
    public void create (Contact contact) {

        ContactValidatorResult validatorResult = contactValidator.validate(contact);
        if(validatorResult.getValid()) {
            contactRepository.save(contact);
        } else if (validatorResult.getException() != null) {
            throw validatorResult.getException();
        }

    }

    /**
     * Update a Contact with given Id {@link NotFoundException} will be thrown if it is not already exist.
     * Validate before saving.
     *
     * @param contact
     */
    @Override
    public void update(UUID id , Contact contact) {
        ContactValidatorResult validatorResult = contactValidator.validate(contact);

        if(validatorResult.getValid()) {
            Optional<Contact> existContact = contactRepository.findById(id);
            if(existContact.isPresent()) {
                Contact contactEntity = existContact.get();
                BeanUtils.copyProperties(contact, contactEntity, "id");
                contactRepository.save(contactEntity);
            } else {
                throw new NotFoundException(String.format("Contact cannot be found with given Id [%s]", id.toString()));
            }
        } else if (validatorResult.getException() != null) {
            throw validatorResult.getException();
        }
    }

    /**
     * Delete a {@link AddressBook}
     *
     * @param id
     */
    @Override
    public void delete(UUID id) {
        Optional<AddressBook> existAddressBook = addressBookRepository.findById(id);
        existAddressBook.ifPresent(addressBook -> addressBookRepository.delete(addressBook));
    }

    /**
     * Delete a {@link Contact}
     *
     * @param id
     */
    @Override
    public void deleteContact(UUID id) {
        Optional<Contact> existContact = contactRepository.findById(id);
        existContact.ifPresent(contact -> contactRepository.delete(contact));
    }

}
