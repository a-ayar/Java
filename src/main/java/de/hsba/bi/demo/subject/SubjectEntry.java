package de.hsba.bi.demo.subject;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import de.hsba.bi.demo.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class SubjectEntry {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Subject subject;

    @Basic(optional = false)
    private BigDecimal amount;

    private String description;

    @ManyToOne(optional = false)
    private User creditor;

    @ManyToMany
    private Set<User> debitors;

    public SubjectEntry(BigDecimal amount, String description, User creditor, Set<User> debitors) {
        this.amount = amount;
        this.description = description;
        this.creditor = creditor;
        this.debitors = debitors;
    }

    public Set<User> getDebitors() {
        if (debitors == null) {
            debitors = new HashSet<>();
        }
        return debitors;
    }

    public String getDebitorNames() {
        return getDebitors().stream().sorted().map(User::getName).collect(Collectors.joining(", "));
    }
}
