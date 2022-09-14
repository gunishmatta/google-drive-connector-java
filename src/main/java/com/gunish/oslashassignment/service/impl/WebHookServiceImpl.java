package com.gunish.oslashassignment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.drive.model.Change;
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
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebHookServiceImpl implements WebHookService {
    private final GoogleDriveConfig drive;


    /**
     * @param token
     * @param batchSize
     * @return List<Change>
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws URISyntaxException
     * Lists changes in a file using batch
     */
    public List<Change> listChanges(String token, int batchSize) throws GeneralSecurityException, IOException, URISyntaxException {

        int batchItemCount = 0;

        List<Change> changes = new ArrayList<>();
        String pageToken = token;

        while (batchSize<=1000) {

            ChangeList changeList = drive.getInstance().changes().list(token)
                    .execute();

            for (Change change : changeList.getChanges()) {
                // Process change
                batchItemCount++;
                log.info("current batch item {}",batchItemCount);
                changes.add(change);
            }
            if (changeList.getNewStartPageToken() != null) {
                // Last page, save this token for the next polling interval
                token = changeList.getNewStartPageToken();
            }
            pageToken = changeList.getNextPageToken();
            if (pageToken == null) {
                pageToken = token;
            }


            if (batchItemCount == batchSize) {
                writeChangesToFile(changes);
                batchSize= batchSize * 10;
            }
        }

        return changes;
    }

    /**
     * @param changes
     * @throws IOException
     * Writes changes to a file
     */
    private void writeChangesToFile(List<Change> changes) throws IOException {
      //TODO:: Enrich this code
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
