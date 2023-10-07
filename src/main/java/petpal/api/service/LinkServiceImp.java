package petpal.api.service;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.contentSources.B2ContentSource;
import com.backblaze.b2.client.contentSources.B2ContentTypes;
import com.backblaze.b2.client.contentSources.B2FileContentSource;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2FileVersion;
import com.backblaze.b2.client.structures.B2UploadFileRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LinkServiceImp implements LinkService {

    @Value("${bucket.id}")
    String bucketId;

    final B2StorageService b2StorageService;

    @Autowired
    public LinkServiceImp(B2StorageService b2StorageService) {
        this.b2StorageService = b2StorageService;
    }

    @Override
    public String uploadPicture(MultipartFile file) throws B2Exception {
        B2StorageClient client = b2StorageService.getClient();
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            String fileName = UUID.randomUUID().toString() + extension;
            File tempFile = File.createTempFile("temp-", null);
            file.transferTo(tempFile);
            B2ContentSource source = B2FileContentSource.build(tempFile);
            B2UploadFileRequest request = B2UploadFileRequest.
                    builder(bucketId, fileName, B2ContentTypes.B2_AUTO, source)
                    .setCustomField("color", "green")
                    .build();

            B2FileVersion fileVersion = client.uploadSmallFile(request);
            tempFile.delete();

            return "https://f005.backblazeb2.com/file/petpal/" + fileVersion.getFileName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
