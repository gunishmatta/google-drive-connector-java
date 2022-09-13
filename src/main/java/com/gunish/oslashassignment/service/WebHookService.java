package com.gunish.oslashassignment.service;

import com.google.api.services.drive.model.Change;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface WebHookService {
    List<Change> listChanges(String token, int batchSize) throws GeneralSecurityException, IOException, URISyntaxException;


}
