package com.bacuti.service;

import java.net.URL;

public interface AWSService {

    URL generatePresignedUrl(String bucketName, String objectKey, int expirationInMinutes);
}
