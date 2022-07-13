package de.hsba.bi.demo.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query("select distinct j from Subject j join j.entries e where e.description like %:search%")
    List<Subject> findByEntryDescription(@Param("search") String search);
}
