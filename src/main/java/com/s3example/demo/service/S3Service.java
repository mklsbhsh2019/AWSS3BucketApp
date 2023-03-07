package com.s3example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3Client;

    public List<S3ObjectSummary> listObjects(String bucketName) {
        try {
            ObjectListing objectListing = amazonS3Client.listObjects(bucketName);
            return objectListing.getObjectSummaries();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
