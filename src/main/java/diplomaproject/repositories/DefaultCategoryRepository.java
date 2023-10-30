package diplomaproject.repositories;

import diplomaproject.models.DefaultCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DefaultCategoryRepository extends JpaRepository<DefaultCategory, Long> {
}
