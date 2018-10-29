package org.imaginea.multitenancy.service;

import org.imaginea.multitenancy.database.lms.model.ContactList;
import org.springframework.data.domain.Page;

public interface ListService {

  Page<ContactList> getLists(int page, int limit);

  ContactList create(ContactList contactList);

  ContactList findById(Long id);

  ContactList update(Long listId, ContactList user);

  void delete(Long id);
}
