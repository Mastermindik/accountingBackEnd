package diplomaproject.controllers;

import diplomaproject.DTO.AppSuccess;
import diplomaproject.DTO.category.AddCategoryDTO;
import diplomaproject.DTO.category.CategoriesDTO;
import diplomaproject.DTO.category.CustomCategoryDTO;
import diplomaproject.exeptions.AppError;
import diplomaproject.services.categoryService.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    private CategoriesDTO getAll(HttpServletRequest request) {
        return categoryService.getAllCategories(request.getHeader("Authorization").substring(7));
    }

    @GetMapping("/allCustom")
    private List<CustomCategoryDTO> getAllCustom(HttpServletRequest request) {
        return categoryService.getCustomCategories(request.getHeader("Authorization").substring(7));
    }

    @PostMapping("/add")
    private ResponseEntity<?> add(@RequestBody AddCategoryDTO addCategoryDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        try {
            categoryService.addCategory(addCategoryDTO, token);
            return new ResponseEntity<>(new AppSuccess("Category created"), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.CONFLICT.value(), "This category is already exist"), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<?> delete(@PathVariable(name = "id") long id) {
        try {
            categoryService.deleteCategoryById(id);
            return new ResponseEntity<>(new AppSuccess("Category deleted"), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Something was wrong"), HttpStatus.BAD_REQUEST);
        }
    }
}
