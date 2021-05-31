package unit;

import addressbook.model.impl.AddressBook;
import addressbook.model.impl.Contact;
import addressbook.service.addressbook.contactvalidator.ContactValidator;
import addressbook.service.addressbook.contactvalidator.impl.DefaultContactValidator;
import addressbook.service.exception.InvalidException;
import addressbook.service.exception.NotFoundException;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class DefaultContactValidatorTest {


    private ContactValidator validator;

    @BeforeEach
    private void beforeTest () {
        validator = new DefaultContactValidator();
    }

    /**
     * Test when the Contact to be saved doesn't has first name. Expect {@link InvalidException} get thrown
     *
     */
    @Test
    void testCreateContact_No_FirstName () {

        Contact contact = new Contact();
        AddressBook addressBook = new AddressBook();
        contact.setAddressBook(addressBook);

         Assertions.assertTrue(validator.validate(contact).getException() instanceof InvalidException);
    }


    /**
     * Test when the Contact to be saved doesn't has email. Expect {@link InvalidException} get thrown
     *
     */
    @Test
    void testCreateContact_No_Email () {
        Contact contact = new Contact();
        contact.setFirstName("John");
        AddressBook addressBook = new AddressBook();
        contact.setAddressBook(addressBook);

        Assertions.assertTrue(validator.validate(contact).getException() instanceof InvalidException);
    }

    /**
     * Test when the Contact to be saved doesn't has email. Expect {@link addressbook.service.exception.NotFoundException} get thrown
     *
     */
    @Test
    void testCreateContact_No_ParentAddressBook () {
        Contact contact = new Contact();
        Assertions.assertTrue(validator.validate(contact).getException() instanceof NotFoundException);
    }


    /**
     * Test when the Contact is already exist in a AddressBook. Expect {@link InvalidException} get thrown
     *
     */
    @Test
    void testCreateContact_Duplicate_Email_Address () {

        Contact contactInAddressBook = new Contact();
        contactInAddressBook.setEmail("tst@tst.com");

        AddressBook addressBook = new AddressBook();
        addressBook.setContacts(Sets.newHashSet(Lists.newArrayList(contactInAddressBook)));
        contactInAddressBook.setAddressBook(addressBook);

        Contact contactToAdd = new Contact();
        contactToAdd.setEmail("tst@tst.com");
        contactToAdd.setAddressBook(addressBook);

        Assertions.assertTrue(validator.validate(contactToAdd).getException() instanceof InvalidException);
    }
}
