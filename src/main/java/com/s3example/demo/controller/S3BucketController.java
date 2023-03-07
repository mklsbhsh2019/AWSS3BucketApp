package com.s3example.demo.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s3example.demo.service.S3Service;
import com.s3example.demo.util.DateGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/bucket")
@RequiredArgsConstructor
@Slf4j
public class S3BucketController {
    private final AmazonS3 amazonS3Client;
    private final S3Service s3Service;
    private final DateGenerator dateGenerator;
    List<Object> jsonResponse;
    @Autowired
    ObjectMapper objectMapper;

    @Value("${aws.bucket.name}")
    String bucketName;

    @GetMapping("/objects")
    public List<Object> readJsonFile2(@RequestParam(name = "start_date") String startDate, @RequestParam(name = "end_date") String endDate) throws IOException, ParseException {
        log.info("Incoming request /api/fetch/v2 with parameters -> start_date: {}, end_date: {}", startDate, endDate);
        jsonResponse = new ArrayList<>();
        List<String> filtersList = dateGenerator.generateDates(startDate, endDate);

        try {
            Set<String> remoteObjects = s3Service.listObjects(bucketName).stream()
                    .map(S3ObjectSummary::getKey).collect(Collectors.toSet());

            //This means there are some objects there in the bucket.
            if (remoteObjects.size() > 0) {
                //We are going to iterate all the objects and going to find our filtered.
                for (String filter : filtersList) {
                    if (remoteObjects.contains(filter)) {
                        S3Object s3object = amazonS3Client.getObject(bucketName, filter);
                        if (Objects.nonNull(s3object)) {
                            try (S3ObjectInputStream inputStream = s3object.getObjectContent()) {
                                readJson(inputStream);
                            }
                        }
                    }
                }

                if (jsonResponse.isEmpty()) {
                    log.info("Haven't found the object within the range that we have given");
                }
                return jsonResponse;
            } else {
                log.info("There are no objects in our given bucket");
                return null;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public void readJson(InputStream inputStream) throws IOException, ParseException {
        try {
            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(result);
            JSONArray dataList = (JSONArray) obj;
            jsonResponse.addAll(dataList);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
