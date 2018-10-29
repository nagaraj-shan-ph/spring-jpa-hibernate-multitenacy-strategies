package org.imaginea.multitenancy.database.lms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.imaginea.multitenancy.database.TenantEntity;

/** The type Contact list. */
@Entity
@Table(name = "contact_lists")
public class ContactList extends TenantEntity {

  @Column(name = "name", nullable = false)
  private String name;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contactList")
  @JsonIgnore
  private Set<Contact> contacts = new HashSet<>();

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets contact list columns.
   *
   * @return the contact list columns
   */
  public Set<Contact> getContacts() {
    return contacts;
  }

  /**
   * Sets contact list columns.
   *
   * @param contacts the contact list columns
   */
  public void setContacts(Set<Contact> contacts) {
    this.contacts = contacts;
  }
}
