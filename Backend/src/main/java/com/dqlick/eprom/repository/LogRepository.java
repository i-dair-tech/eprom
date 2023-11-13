package com.dqlick.eprom.repository;

import com.dqlick.eprom.domain.Log;
import com.dqlick.eprom.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Log entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogRepository extends JpaRepository<Log, Long> , CrudRepository<Log, Long> {


    @Query(value ="SELECT * FROM eprom_log ORDER BY created_date DESC ", nativeQuery = true)
    List<Log> findAllByOrderByDateAsc();
}
