package de.hsba.bi.demo.subject;

import de.hsba.bi.demo.user.User;
import de.hsba.bi.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
public class TestDataCreator {

    private final UserService userService;
    private final SubjectService subjectService;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        if (!userService.findAll().isEmpty()) {
            // prevent initialization if DB is not empty
            return;
        }

        // add some users
        User anne = createUser("Anne", "123456", User.USER_ROLE);
        User benedikt = createUser("Benedikt", "123456", User.USER_ROLE);
        User charlotte = createUser("Charlotte", "123456", User.USER_ROLE);
        createUser("Xenia", "123456", User.USER_ROLE);
        createUser("Yves", "123456", User.USER_ROLE);
        createUser("Zoe", "123456", User.USER_ROLE);
        createUser("admin", "password", User.ADMIN_ROLE);

        Subject subject = new Subject(anne);
        subject.setName("Sommerparty");
        subjectService.addSubjectEntry(subject, new SubjectEntry(new BigDecimal(15), "Drinks", anne, Set.of(anne, benedikt, charlotte)));
        subjectService.addSubjectEntry(subject, new SubjectEntry(new BigDecimal(16), "Kino", charlotte, Set.of(anne, charlotte)));
        subjectService.save(subject);
    }

    private User createUser(String name, String password, String role) {
        return userService.save(new User(name, passwordEncoder.encode(password), role));
    }
}