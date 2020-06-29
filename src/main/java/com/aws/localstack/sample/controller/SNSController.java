package com.aws.localstack.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.localstack.sample.service.SNSService;

@RestController
@RequestMapping(value = "/api/v1/sns/")
public class SNSController {

	@Autowired
	SNSService snsService;
	
	@PostMapping(value = "/")
	public ResponseEntity<String> sendSNSMessage(@RequestBody String message) {
		return new ResponseEntity<String>(snsService.publishMessage(message),HttpStatus.OK);
	}
}
