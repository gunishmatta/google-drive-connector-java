package com.gunish.oslashassignment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.drive.model.ChangeList;
import com.gunish.oslashassignment.config.GoogleDriveConfig;
import com.gunish.oslashassignment.service.WebHookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebHookServiceImpl implements WebHookService {
    private final GoogleDriveConfig drive;


    public ChangeList listChanges(String token,int batchSize) throws GeneralSecurityException, IOException, URISyntaxException {

        int batchItemCount = 0;

        ChangeList changes = null;

        while (batchItemCount++<=batchSize) {
            String pageToken = token;
            while (pageToken != null) {
                changes = drive.getInstance().changes().list(pageToken)
                        .execute();

                if (changes.getNewStartPageToken() != null) {
                    token = changes.getNewStartPageToken();
                }
                pageToken = changes.getNextPageToken();
            }

            writeChangesToFile(changes);
        }

        return changes;
    }

    private void writeChangesToFile(ChangeList changes) throws IOException {
        ObjectMapper mapper = new ObjectMapper();


        ClassPathResource resource = new ClassPathResource("changes.json");
        File file;
        if (resource == null) {
            Path source = Paths.get(this.getClass().getResource("/").getPath());
            Path path = Paths.get(source.toAbsolutePath() + "/response/changes.json");
            file = new File(path.toUri());
        } else {
            file = resource.getFile();
        }

        try {

            mapper.writeValue(file, changes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
