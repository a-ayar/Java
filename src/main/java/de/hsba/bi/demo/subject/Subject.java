package de.hsba.bi.demo.subject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.persistence.*;

import de.hsba.bi.demo.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Subject {

    @Getter
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @Getter
    private User owner;

    @Getter
    @Setter
    @Basic(optional = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "subject")
    @OrderBy
    private List<SubjectEntry> entries;

    public Subject(final User user) {
        this.owner = user;
    }

    public List<SubjectEntry> getEntries() {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        return entries;
    }
    public Map<User, BigDecimal> computeBalance() {
        // map of the balances sorted by name
        Map<User, BigDecimal> balance = new TreeMap<>();
        for (SubjectEntry entry : getEntries()) {
            Set<User> debitors = entry.getDebitors();

            if (debitors.isEmpty()) {
                // ignore entries without debitors
                continue;
            }

            addToBalance(balance, entry.getCreditor(), entry.getAmount());
            BigDecimal debitorAmount = entry.getAmount()
                    .divide(new BigDecimal(debitors.size()), 4, RoundingMode.HALF_UP).negate();
            for (User debitor : debitors) {
                addToBalance(balance, debitor, debitorAmount);
            }
        }

        // round the result to 2 decimals
        for (Map.Entry<User, BigDecimal> mapEntry : balance.entrySet()) {
            mapEntry.setValue(mapEntry.getValue().setScale(2, RoundingMode.HALF_UP));
        }

        return balance;
    }

    private void addToBalance(Map<User, BigDecimal> balance, User user, BigDecimal amount) {
        BigDecimal currentAmount = balance.getOrDefault(user, BigDecimal.ZERO);
        balance.put(user, currentAmount.add(amount));
    }

    public boolean isOwnedByCurrentUser() {
        return this.owner != null && this.owner.getName().equals(User.getCurrentUserName());
    }
}
