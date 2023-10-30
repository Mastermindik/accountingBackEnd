package diplomaproject.services.accountPictureService;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AccountPictureService {
    byte[] getImageById(Long id);
    long saveAccountPicture(MultipartFile file) throws IOException;
}
