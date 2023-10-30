package diplomaproject.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class AccountPicture {
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] bytes;

    public AccountPicture(byte[] bytes) {
        this.bytes = bytes;
    }
}
