package addressbook.service.addressbook.contactvalidator;

import addressbook.model.impl.Contact;



public interface ContactValidator {


    ContactValidatorResult validate(Contact contact);
}
