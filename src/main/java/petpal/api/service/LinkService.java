package petpal.api.service;

import com.backblaze.b2.client.exceptions.B2Exception;
import org.springframework.web.multipart.MultipartFile;

public interface LinkService {
    String uploadPicture(MultipartFile file) throws B2Exception;
}
