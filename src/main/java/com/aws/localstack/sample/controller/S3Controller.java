package com.aws.localstack.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aws.localstack.sample.service.UploadService;

@RestController
@RequestMapping(value = "/api/v1/s3/")
public class S3Controller {

	@Autowired
	private UploadService uploadService;
	
	@PostMapping(path="/upload")
	public void fileUpload(@RequestParam("filename") String filename, @RequestParam("filePath") String filePath) {
		uploadService.uploadFile(filename, filePath);
	}
}
