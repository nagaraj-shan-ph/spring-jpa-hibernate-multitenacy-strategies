package org.imaginea.multitenancy.service.impl;

import java.util.function.Supplier;
import org.imaginea.multitenancy.database.lms.model.ContactList;
import org.imaginea.multitenancy.database.lms.repository.ContactListRepository;
import org.imaginea.multitenancy.exception.NotFoundException;
import org.imaginea.multitenancy.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ListServiceImpl implements ListService {

  @Autowired
  private ContactListRepository repository;

  @Override
  public Page<ContactList> getLists(int page, int limit) {
    Pageable pageableRequest = PageRequest.of(page, limit);
    return repository.findAll(pageableRequest);
  }

  @Override
  public ContactList create(ContactList contactList) {
    return repository.save(contactList);
  }

  @Override
  public ContactList findById(Long id) {
    return repository.findById(id).orElseThrow((Supplier<RuntimeException>) NotFoundException::new);
  }

  @Override
  public ContactList update(Long listId, ContactList contactList) {
    ContactList fromDb = findById(listId);
    update(contactList, fromDb);
    return repository.save(fromDb);
  }

  @Override
  public void delete(Long id) {
    if (repository.existsById(id)) {
      repository.deleteById(id);
    }
  }

  private void update(ContactList contactList, ContactList fromDb) {
    fromDb.setName(contactList.getName());
  }
}
