package com.gunish.oslashassignment.service.impl;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.Change;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.Channel;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.StartPageToken;
import com.gunish.oslashassignment.config.GoogleDriveConfig;
import com.gunish.oslashassignment.service.GoogleDriveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoogleDriveServiceImpl implements GoogleDriveService {


    private final GoogleDriveConfig drive;


    public String getIdFromName(String name) throws IOException, GeneralSecurityException {
        FileList result = drive.getInstance().files().list()
                .setSpaces("drive")
                .setFields("nextPageToken, files(id, name)")
                .execute();
        for (com.google.api.services.drive.model.File file : result.getFiles()) {
            if (file.getName().equals(name))
                return file.getId();
        }
        return "root";
    }

    public List<com.google.api.services.drive.model.File> listEverything() throws IOException, GeneralSecurityException {
        FileList result = drive.getInstance().files().list()
                .setPageSize(1000)
                .setFields("nextPageToken, files(id, name, size, thumbnailLink, shared)")
                .execute();
        return result.getFiles();
    }

    public List<com.google.api.services.drive.model.File> listFolderContent(String parentName) throws IOException, GeneralSecurityException {
        String parentId = parentName == null ? "root" : getIdFromName(parentName);
        String query = "'" + parentId + "' in parents";
        FileList result = drive.getInstance().files().list()
                .setQ(query)
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        return result.getFiles();
    }


    public void watchFolder(String folderName) throws Exception {
        String parentId = folderName == null ? "root" : getIdFromName(folderName);
        String query = "'" + parentId + "' in parents";
        StartPageToken token = drive.getInstance().changes().getStartPageToken().execute();
        BatchRequest request = drive.getInstance().batch();

        Channel channel=new Channel();
        channel.setToken(token.getStartPageToken());
        channel.setKind("api#channel");
        channel.setId(UUID.randomUUID().toString());
        channel.setResourceId(parentId);
        channel.setType("web_hook");
        //update this to a ngrok forwarded https url, required for the webhook
        channel.setAddress("https://1c09-103-21-185-12.in.ngrok.io/notifications");


        drive.getInstance().changes().watch(token.getStartPageToken(), channel).queue(request, new JsonBatchCallback<Channel>() {
            @Override
            public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {
                log.error(e.getMessage());
            }

            @Override
            public void onSuccess(Channel channel, HttpHeaders responseHeaders) throws IOException {
                log.info("channel {}", channel.toPrettyString());
            }
        });



        request.execute();


    }

}
