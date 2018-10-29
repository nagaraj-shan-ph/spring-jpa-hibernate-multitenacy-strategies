package org.imaginea.multitenancy.web.controller.list;

import io.swagger.annotations.Api;
import org.imaginea.multitenancy.database.lms.model.Contact;
import org.imaginea.multitenancy.service.ContactService;
import org.imaginea.multitenancy.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "${application.basePath}/lists/{listId}/contacts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = "ListContacts", description = "List Contacts")
public class ContactController {

  @Autowired
  private ContactService contactService;

  @Autowired
  private ListService listService;

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Page<Contact>> getLists(@PathVariable("listId") Long listId, @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "limit", defaultValue = "25") int limit) {
    return ResponseEntity.ok(contactService.getContacts(listId, page, limit));
  }

  @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Contact> create(@PathVariable("listId") Long listId, @RequestBody Contact contact) {
    return ResponseEntity.ok(contactService.create(listId, contact));
  }


  @GetMapping(path = {"/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Contact> findOne(@PathVariable("listId") Long listId, @PathVariable("id") Long id) {
    return ResponseEntity.ok(contactService.findByListIdAndId(listId, id));
  }

  @PutMapping(path = {"message/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Contact> sendMessage(@PathVariable("id") String message) {
    return ResponseEntity.ok().build();
  }

  @PutMapping(path = {"/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Contact> update(@PathVariable("listId") Long listId, @PathVariable("id") Long id, @RequestBody Contact contact) {
    return ResponseEntity.ok(contactService.update(listId, id, contact));
  }

  @DeleteMapping(path = {"/{id}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<?> delete(@PathVariable("listId") Long listId, @PathVariable("id") Long id) {
    contactService.delete(listId, id);
    return ResponseEntity.noContent().build();
  }
}
