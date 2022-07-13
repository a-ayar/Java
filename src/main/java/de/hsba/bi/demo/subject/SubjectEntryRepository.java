package de.hsba.bi.demo.subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectEntryRepository extends JpaRepository<SubjectEntry, Long> {
}
