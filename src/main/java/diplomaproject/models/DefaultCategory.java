package diplomaproject.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class DefaultCategory extends MainCategory {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    public DefaultCategory(String name, TransactionType type) {
        this.name = name;
        this.type = type;
    }
}
