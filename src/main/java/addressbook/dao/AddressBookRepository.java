package addressbook.dao;

import addressbook.model.impl.AddressBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressBookRepository extends JpaRepository<AddressBook, UUID> {
}
