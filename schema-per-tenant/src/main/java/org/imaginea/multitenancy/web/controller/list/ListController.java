package org.imaginea.multitenancy.web.controller.list;

import io.swagger.annotations.Api;
import org.imaginea.multitenancy.database.lms.model.ContactList;
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
@RequestMapping(value = "${application.basePath}/lists", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = "Lists", description = "Lists")
public class ListController {

  @Autowired
  private ListService listService;

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Page<ContactList>> getLists(@RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "limit", defaultValue = "25") int limit) {
    return ResponseEntity.ok(listService.getLists(page, limit));
  }

  @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ContactList> create(@RequestBody ContactList contactList) {
    return ResponseEntity.ok(listService.create(contactList));
  }

  @PutMapping(path = {"/{listId}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ContactList> update(@PathVariable("listId") Long id, @RequestBody ContactList contactList) {
    return ResponseEntity.ok(listService.update(id, contactList));
  }

  @GetMapping(path = {"/{listId}"}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<ContactList> findOne(@PathVariable("listId") Long id) {
    return ResponseEntity.ok(listService.findById(id));
  }

  @DeleteMapping(path = {"/{listId}"})
  public ResponseEntity<?> delete(@PathVariable("listId") Long id) {
    listService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
