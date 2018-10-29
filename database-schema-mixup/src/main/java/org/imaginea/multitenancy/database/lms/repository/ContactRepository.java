package org.imaginea.multitenancy.database.lms.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.imaginea.multitenancy.database.lms.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for the {@link Contact} JPA entity. Any custom methods, not already defined in
 * {@link JpaRepository}, are to be defined here.
 */
public interface ContactRepository extends JpaRepository<Contact, Long> {

  Optional<Contact> findByIdAndContactListId(Long id, Long listId);

  Page<Contact> findAllByContactListId(Long listId, Pageable pageable);

  Stream<Contact> findAllByContactListId(Long listId);

  @Modifying
  @Query(value = "UPDATE contacts SET custom_data = custom_data - :column " +
          "where list_id=:id", nativeQuery = true)
  void deleteCustomColumns(@Param("id") Long listId,@Param("column") String columnTobeDeleted);

  @Modifying
  @Query(value = "UPDATE contacts SET custom_data = custom_data - :column || jsonb_build_object(:newColumn, custom_data->:column)" +
          "where custom_data ->>:column IS NOT NULL AND list_id=:id", nativeQuery = true)
  void renameCustomColumns(@Param("id") Long listId,@Param("column") String column,@Param("newColumn") String newColumn);
}
