package com.gunish.oslashassignment.service;

import com.google.api.services.drive.model.ChangeList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

public interface WebHookService {
    ChangeList listChanges(String token,int batchSize) throws GeneralSecurityException, IOException, URISyntaxException;


}
