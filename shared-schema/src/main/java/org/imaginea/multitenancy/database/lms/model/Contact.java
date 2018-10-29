package org.imaginea.multitenancy.database.lms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.imaginea.multitenancy.database.TenantEntity;

/** The type Contact. */
@Entity
@Table(name = "contacts")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact extends TenantEntity {

  @JsonProperty("First Name")
  @Column(name = "first_name")
  private String firstName;

  @JsonProperty("Middle Name")
  @Column(name = "middle_name")
  private String middleName;

  @JsonProperty("Last Name")
  @Column(name = "last_name")
  private String lastName;

  @JsonProperty("Address 1")
  @Column(name = "address1")
  private String address1;

  @JsonProperty("Address 2")
  @Column(name = "address2")
  private String address2;

  @JsonProperty("City")
  @Column(name = "city")
  private String city;

  @JsonProperty("State/Province")
  @Column(name = "state")
  private String state;

  @JsonProperty("Postal Code")
  @Column(name = "zip")
  private String zip;

  @JsonProperty("Country")
  @Column(name = "country")
  private String country;

  @JsonProperty("Company")
  @Column(name = "company")
  private String company;

  @JsonProperty("Email")
  @Column(name = "email")
  private String email;

  @JsonProperty("Mobile")
  @Column(name = "mobile")
  private String mobile;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "list_id", nullable = false)
  @JsonIgnore
  private ContactList contactList;
  /**
   * Gets first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets first name.
   *
   * @param firstName the first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets middle name.
   *
   * @return the middle name
   */
  public String getMiddleName() {
    return middleName;
  }

  /**
   * Sets middle name.
   *
   * @param middleName the middle name
   */
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  /**
   * Gets last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets last name.
   *
   * @param lastName the last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets address 1.
   *
   * @return the address 1
   */
  public String getAddress1() {
    return address1;
  }

  /**
   * Sets address 1.
   *
   * @param address1 the address 1
   */
  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  /**
   * Gets address 2.
   *
   * @return the address 2
   */
  public String getAddress2() {
    return address2;
  }

  /**
   * Sets address 2.
   *
   * @param address2 the address 2
   */
  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  /**
   * Gets city.
   *
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * Sets city.
   *
   * @param city the city
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Gets state.
   *
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * Sets state.
   *
   * @param state the state
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Gets zip.
   *
   * @return the zip
   */
  public String getZip() {
    return zip;
  }

  /**
   * Sets zip.
   *
   * @param zip the zip
   */
  public void setZip(String zip) {
    this.zip = zip;
  }

  /**
   * Gets country.
   *
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets country.
   *
   * @param country the country
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Gets company.
   *
   * @return the company
   */
  public String getCompany() {
    return company;
  }

  /**
   * Sets company.
   *
   * @param company the company
   */
  public void setCompany(String company) {
    this.company = company;
  }

  /**
   * Gets email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets email.
   *
   * @param email the email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets mobile.
   *
   * @return the mobile
   */
  public String getMobile() {
    return mobile;
  }

  /**
   * Sets mobile.
   *
   * @param mobile the mobile
   */
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /**
   * Gets contact list.
   *
   * @return the contact list
   */
  public ContactList getContactList() {
    return contactList;
  }

  /**
   * Sets contact list.
   *
   * @param contactList the contact list
   */
  public void setContactList(ContactList contactList) {
    this.contactList = contactList;
  }
}
