package com.bacuti.web.rest;

import com.bacuti.service.AWSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;

@RestController
@RequestMapping("/api/v1/aws")
public class AWSController {

    public static Integer EXPIRATION_IN_MINUTES = 2;

    @Autowired
    private AWSService awsService;

    @GetMapping("/presigned-url")
    public URL getPresignedUrl(@RequestParam String bucketName, @RequestParam String objectKey) {
        return awsService.generatePresignedUrl(bucketName, objectKey, EXPIRATION_IN_MINUTES);
    }
}
