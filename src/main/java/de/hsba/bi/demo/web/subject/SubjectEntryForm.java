package de.hsba.bi.demo.web.subject;

import de.hsba.bi.demo.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SubjectEntryForm {

    @NotNull(message = "Bitte einen Betrag eingeben")
    private BigDecimal amount;

    @NotEmpty(message = "Bitte eine Beschreibung eingeben")
    private String description;

    @NotNull(message = "Bitte einen Namen wählen")
    private User creditor;

    @NotNull
    @Size(min = 1, message = "Bitte einen oder mehrere Namen wählen")
    private Set<User> debitors;

    public Set<User> getDebitors() {
        if (debitors == null) {
            debitors = new HashSet<>();
        }
        return debitors;
    }
}