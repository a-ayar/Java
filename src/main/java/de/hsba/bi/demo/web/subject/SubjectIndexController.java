package de.hsba.bi.demo.web.subject;

import de.hsba.bi.demo.subject.Subject;
import de.hsba.bi.demo.subject.SubjectService;
import de.hsba.bi.demo.user.User;
import de.hsba.bi.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectIndexController {
    private final SubjectService subjectService;
    private final SubjectFormConverter formConverter;
    private final UserService userService;

    @GetMapping
    public String index(Model model, @RequestParam(value = "suche", required = false, defaultValue = "") String search) {
        model.addAttribute("subjects", subjectService.findSubjects(search));
        model.addAttribute("suche", search);
        model.addAttribute("subjectForm", new SubjectForm());
        return "subjects/index";
    }

    @PostMapping
    public String create(@ModelAttribute("subjectForm") @Valid SubjectForm subjectForm, BindingResult subjectBinding) {
        if (subjectBinding.hasErrors()) {
            return "subjects/index";
        }
        User currentUser = userService.findCurrentUser();
        Subject subject = subjectService.save(formConverter.update(new Subject(currentUser), subjectForm));
        return "redirect:/subjects/" + subject.getId();
    }
}
