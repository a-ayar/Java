package de.hsba.bi.demo.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository repository;
    private final SubjectEntryRepository entryRepository;

    public Subject save(Subject subject) {
        return repository.save(subject);
    }

    public Subject getSubject(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void addSubjectEntry(Subject subject, SubjectEntry entry) {
        entry.setSubject(subject);
        subject.getEntries().add(entry);
    }

    public List<Subject> findSubjects(String search) {
        return search.isBlank() ? repository.findAll() : repository.findByEntryDescription(search.trim());
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public SubjectEntry findEntry(Long id) {
        return entryRepository.findById(id).orElse(null);
    }

    public SubjectEntry save(SubjectEntry entry) {
        return entryRepository.save(entry);
    }
}