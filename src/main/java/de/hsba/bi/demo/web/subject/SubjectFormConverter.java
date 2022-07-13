package de.hsba.bi.demo.web.subject;

import de.hsba.bi.demo.subject.Subject;
import de.hsba.bi.demo.subject.SubjectEntry;
import org.springframework.stereotype.Component;

@Component
public class SubjectFormConverter {

    SubjectForm toForm(Subject subject) {
        SubjectForm form = new SubjectForm();
        form.setName(subject.getName());
        return form;
    }

    Subject update(Subject subject, SubjectForm form) {
        subject.setName(form.getName());
        return subject;
    }

    SubjectEntryForm toForm(SubjectEntry entry) {
        SubjectEntryForm form = new SubjectEntryForm();
        form.setAmount(entry.getAmount());
        form.setDescription(entry.getDescription());
        form.setCreditor(entry.getCreditor());
        form.setDebitors(entry.getDebitors());
        return form;
    }

    SubjectEntry update(SubjectEntry entry, SubjectEntryForm form) {
        entry.setAmount(form.getAmount());
        entry.setDescription(form.getDescription());
        entry.setCreditor(form.getCreditor());
        entry.setDebitors(form.getDebitors());
        return entry;
    }
}
