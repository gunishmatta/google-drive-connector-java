package com.gunish.oslashassignment.controller;

import com.gunish.oslashassignment.service.WebHookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Slf4j
@RestController
public class WebhookController {

    private final WebHookService webHookService;

    public WebhookController(WebHookService webHookService) {
        this.webHookService = webHookService;
    }

    @PostMapping("/notifications")
    @ResponseBody
    public ResponseEntity<String> driveNotificationHandler(@RequestHeader(required = false) Map<String, String> headers) throws GeneralSecurityException, IOException, URISyntaxException {
        webHookService.listChanges(headers.get("X-Goog-Channel-Token"),10);
        return ResponseEntity.ok("Success");
    }
}
