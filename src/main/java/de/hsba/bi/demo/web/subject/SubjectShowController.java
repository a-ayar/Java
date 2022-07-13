package de.hsba.bi.demo.web.subject;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.hsba.bi.demo.subject.Subject;
import de.hsba.bi.demo.subject.SubjectEntry;
import de.hsba.bi.demo.subject.SubjectService;
import de.hsba.bi.demo.user.User;
import de.hsba.bi.demo.user.UserService;
import de.hsba.bi.demo.web.ForbiddenException;
import de.hsba.bi.demo.web.NotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/subjects/{id}")
@RequiredArgsConstructor
public class SubjectShowController {


    private final SubjectService subjectService;
    private final UserService userService;
    private final SubjectFormConverter formConverter;


    @ModelAttribute("users")
    public List<User> getUsers() {
        return userService.findUsers();
    }

    @ModelAttribute("subject")
    public Subject getSubject(@PathVariable("id") Long id) {
        Subject subject = subjectService.getSubject(id);
        if (subject == null) {
            throw new NotFoundException();
        }
        return subject;
    }

    @ExceptionHandler(NotFoundException.class)
    public String notFound() {
        return "subjects/notFound";
    }

    @GetMapping
    public String show(@PathVariable("id") Long id, Model model) {
        model.addAttribute("subjectForm", formConverter.toForm(getSubject(id)));
        model.addAttribute("subjectEntryForm", new SubjectEntryForm());
        return "subjects/show";
    }

    @PostMapping
    public String change(Model model, @PathVariable("id") Long id,
                         @ModelAttribute("subjectForm") @Valid SubjectForm subjectForm, BindingResult subjectBinding) {
        if (subjectBinding.hasErrors()) {
            model.addAttribute("subjectEntryForm", new SubjectEntryForm());
            return "subjects/show";
        }
        Subject subject = getSubject(id);
        if (!subject.isOwnedByCurrentUser()) {
            throw new ForbiddenException();
        }
        subjectService.save(formConverter.update(subject, subjectForm));
        return "redirect:/subjects/" + id;
    }

    @PostMapping(path = "/entries")
    public String addEntry(Model model, @PathVariable("id") Long id,
                           @ModelAttribute("subjectEntryForm") @Valid SubjectEntryForm entryForm, BindingResult entryBinding) {
        Subject subject = getSubject(id);
        if (entryBinding.hasErrors()) {
            model.addAttribute("subjectForm", formConverter.toForm(subject));
            return "subjects/show";
        }
        subjectService.addSubjectEntry(subject, formConverter.update(new SubjectEntry(), entryForm));
        return "redirect:/subjects/" + id;
    }

    @PostMapping(path = "/delete")
    public String delete(@PathVariable("id") Long id) {
        Subject subject = getSubject(id);
        if (!subject.isOwnedByCurrentUser()) {
            throw new ForbiddenException();
        }
        subjectService.delete(id);
        return "redirect:/subjects/";
    }
}
