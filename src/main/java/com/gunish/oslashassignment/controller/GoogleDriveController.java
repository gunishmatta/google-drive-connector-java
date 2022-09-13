package com.gunish.oslashassignment.controller;

import com.google.api.services.drive.model.File;
import com.gunish.oslashassignment.service.GoogleDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
public class GoogleDriveController {

    @Autowired
    private GoogleDriveService service;

    @GetMapping({"/files"})
    public ResponseEntity<List<File>> listEverything() throws IOException, GeneralSecurityException {
        List<com.google.api.services.drive.model.File> files = service.listEverything();
        return ResponseEntity.ok(files);
    }


    @GetMapping("/watchfolder")
    public ResponseEntity<String> watchFolder(@RequestParam String folderName) throws Exception {
        service.watchFolder(folderName);
        return ResponseEntity.ok("Resource Watching Now");
    }


}
