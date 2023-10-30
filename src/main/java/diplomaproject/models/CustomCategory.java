package diplomaproject.models;

import diplomaproject.DTO.category.AddCategoryDTO;
import diplomaproject.DTO.category.CustomCategoryDTO;
import diplomaproject.DTO.category.DeleteCategoryDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class CustomCategory extends MainCategory {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public static CustomCategory fromAddCategoryDTO(AddCategoryDTO addCategoryDTO) {
        return new CustomCategory(addCategoryDTO.getName(), TransactionType.valueOf(addCategoryDTO.getType().toUpperCase()));
    }

    public static CustomCategory fromDeleteCategoryDTO(DeleteCategoryDTO deleteCategoryDTO) {
        return new CustomCategory(deleteCategoryDTO.getName(), TransactionType.valueOf(deleteCategoryDTO.getType().toUpperCase()));
    }

    public CustomCategoryDTO toCustomCategoryDTO() {
        return new CustomCategoryDTO(this.id, this.name, this.type.name().toLowerCase());
    }

    public CustomCategory(String name, TransactionType type) {
        this.name = name;
        this.type = type;
    }
}