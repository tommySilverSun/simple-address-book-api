package addressbook.service.addressbook.contactvalidator.impl;

import addressbook.model.impl.AddressBook;
import addressbook.model.impl.Contact;
import addressbook.service.addressbook.contactvalidator.ContactValidator;
import addressbook.service.addressbook.contactvalidator.ContactValidatorResult;
import addressbook.service.exception.InvalidException;
import addressbook.service.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DefaultContactValidator implements ContactValidator {

    @Override
    public ContactValidatorResult validate(Contact contact) {
        if (contact.getAddressBook() == null) {
            return new ContactValidatorResult(false, new NotFoundException("Contact doesn't belong to any AddressBook"));
        }

        if (contact.getFirstName() == null) {
            return new ContactValidatorResult(false, new InvalidException("Contact must contain first name"));
        }

        if (contact.getEmail() == null) {
            return new ContactValidatorResult(false, new InvalidException("Contact must contain email address"));
        }

        AddressBook addressBook = contact.getAddressBook();

        if(!addressBook.getContacts().isEmpty() && !addressBook.getContacts().add(contact)) {
            return new ContactValidatorResult(false, new InvalidException("Contact is already exist in the AddressBook"));
        }

        return new ContactValidatorResult(true, null);
    }
}
