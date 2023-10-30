package diplomaproject.services.accountPictureService;

import diplomaproject.models.AccountPicture;
import diplomaproject.repositories.AccountPictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AccountPictureServiceImpl implements AccountPictureService{
    private final AccountPictureRepository accountPictureRepository;
    @Override
    @Transactional(readOnly = true)
    public byte[] getImageById(Long id) {
        return accountPictureRepository.findById(id).get().getBytes();
    }

    @Override
    @Transactional
    public long saveAccountPicture(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        AccountPicture accountPicture = new AccountPicture(bytes);
        accountPictureRepository.save(accountPicture);
        return accountPicture.getId();
    }
}
