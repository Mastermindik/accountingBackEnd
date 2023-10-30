package diplomaproject.controllers;

import diplomaproject.DTO.AppSuccess;
import diplomaproject.DTO.user.*;
import diplomaproject.exeptions.AppError;
import diplomaproject.services.accountPictureService.AccountPictureService;
import diplomaproject.services.accountService.AccountServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("user")
@CrossOrigin
@RequiredArgsConstructor
public class AccountController {
    private final AccountServiceImpl accountService;
    private final AccountPictureService accountPictureService;

    @GetMapping("test")
    private List<AccountDTO> test() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/current")
    private AccountDTO getCurrent(HttpServletRequest request) {
        return accountService.getAccountFromToken(request.getHeader("Authorization"));
    }

    @PostMapping("login")
    private ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest, HttpServletResponse response) {
        try {
            String token = accountService.loginAccount(jwtRequest);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Incorrect email or password"), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("register")
    private ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO, HttpServletResponse response) {
        try {
            String token = accountService.registerAccount(registerDTO);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.CONFLICT.value(), "User with this email already exist"), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("logout")
    private ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        accountService.logoutAccount(request, response);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("updateAccount")
    private ResponseEntity<?> updateAccount(@RequestBody UpdateAccountDTO updateAccountDTO, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            accountService.updateAccount(updateAccountDTO, token);
            return new ResponseEntity<>(new AppSuccess("Account changing success"), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @PatchMapping("updateAccountPicture")
    private ResponseEntity<?> updateAccountPicture(@RequestPart("file") MultipartFile file, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            accountService.updateAccountPicture(file, token);
            return new ResponseEntity<>(new AppSuccess("Avatar updated successfully"), HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }

    @GetMapping("/image/{id}")
    private ResponseEntity<?> getImage(@PathVariable("id") long id) {
        byte[] imageBytes = accountPictureService.getImageById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        // Зображення віідображається а не завантажується
        headers.setContentDispositionFormData("inline", "image.jpg");

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
