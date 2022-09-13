package com.gunish.oslashassignment.service;

import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface GoogleDriveService {
     String getIdFromName(String name) throws IOException, GeneralSecurityException;

     List<File> listEverything() throws IOException, GeneralSecurityException;

     void watchFolder(String folderName) throws Exception;

    }
