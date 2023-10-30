package diplomaproject.repositories;

import diplomaproject.models.CustomCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomCategoryRepository extends JpaRepository<CustomCategory, Long> {
    List<CustomCategory> findCustomCategoriesByAccount_Email(String email);
}
