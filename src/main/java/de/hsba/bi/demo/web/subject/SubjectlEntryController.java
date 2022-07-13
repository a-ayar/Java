package de.hsba.bi.demo.web.subject;

import de.hsba.bi.demo.subject.SubjectEntry;
import de.hsba.bi.demo.subject.SubjectService;
import de.hsba.bi.demo.user.User;
import de.hsba.bi.demo.user.UserService;
import de.hsba.bi.demo.web.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/entries/{id}")
@PreAuthorize("authenticated")
@RequiredArgsConstructor
public class SubjectlEntryController {


    private final SubjectService subjectService;
    private final UserService userService;
    private final SubjectFormConverter formConverter;

    @ModelAttribute("users")
    public List<User> getUsers() {
        return userService.findAll();
    }

    @ModelAttribute("entry")
    public SubjectEntry getEntry(@PathVariable("id") Long id) {
        SubjectEntry entry = subjectService.findEntry(id);
        if (entry == null) {
            throw new NotFoundException();
        }
        return entry;
    }

    @GetMapping
    public String show(Model model, @PathVariable("id") Long id) {
        model.addAttribute("subjectEntryForm", formConverter.toForm(getEntry(id)));
        return "subjects/entry";
    }

    @PostMapping
    public String update(
            @PathVariable("id") Long id,
            @ModelAttribute("subjectEntryForm") @Valid SubjectEntryForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "subjects/entry";
        }
        SubjectEntry entry = getEntry(id);
        subjectService.save(formConverter.update(entry, form));
        return "redirect:/subjects/" + entry.getSubject().getId();
    }
}
