package petpal.api.service;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Getter
@Component
public class B2StorageService {

    private B2StorageClient client;

    @Value("${application.key.id}")
    private String applicationKeyId;

    @Value("${application.key}")
    private String applicationKey;

    public B2StorageService() {

    }

    @PostConstruct
    public void init() {
        try {
            client = B2StorageClientFactory
                    .createDefaultFactory()
                    .create(applicationKeyId, applicationKey, "petpal/1.0");
        } catch (Exception e) {
            System.out.println("Failed to initialize B2StorageService: " + e.getMessage());
            throw e;
        }
    }
}
