package org.examples.sb.repositories;

import org.examples.sb.repositories.entities.AuditlogEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

/**
 *
 * @author brijeshdhaker
 */
//@Repository("auditlogRepository")
public interface AuditlogRepository extends PagingAndSortingRepository<AuditlogEntity, Long> {

    @Query("select al from AuditlogEntity al where al.auditTypeName=:type")
    List<AuditlogEntity> findByType(@Param("type") String type);

    @Query("select al from AuditlogEntity al where al.id=:id")
    Optional<AuditlogEntity> findById(@Param("id") Long id);

    @Query("select al from AuditlogEntity al where al.userid=:userid")
    List<AuditlogEntity> findByUser(@Param("userid") String userid);

}
