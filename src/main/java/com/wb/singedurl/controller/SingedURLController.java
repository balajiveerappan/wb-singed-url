package com.wb.singedurl.controller;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

@RestController
@RequestMapping(path = "/protected")
public class SingedURLController {

	Logger logger = LoggerFactory.getLogger(SingedURLController.class);

	public URL url = null;

	@Value("${s3.bucket.name}")
	private String S3_BUCKET;

	@RequestMapping(method = RequestMethod.GET, value = "/pre-singed-url", produces = MediaType.APPLICATION_JSON_VALUE)
	public URL getPreSingedURL(@RequestParam(required = true) String filename) throws IOException {
		AmazonS3 s3client = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider("aws-auth.properties"));
		Region usWest2 = Region.getRegion(Regions.US_EAST_1);
		s3client.setRegion(usWest2);

		try {

			java.util.Date expiration = new java.util.Date();
			long milliSeconds = expiration.getTime();
			milliSeconds += 1000 * 60 * 60; // Add 1 hour.
			expiration.setTime(milliSeconds);
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(S3_BUCKET,
					filename);
			generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
			generatePresignedUrlRequest.setExpiration(expiration);
			url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

			logger.info("Pre-Signed URL =  {}", url.toString());

		} catch (AmazonServiceException exception) {
			logger.error("Error: {}", exception.getErrorMessage());
		} catch (AmazonClientException ace) {
			logger.error("Error: {}", ace.getMessage());
		}
		ResponseEntity<URL> responseEntity = new ResponseEntity<URL>(url, HttpStatus.OK);

		return url;
	}

}
