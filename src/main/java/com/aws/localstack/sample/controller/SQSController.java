package com.aws.localstack.sample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aws.localstack.sample.service.SQSService;

@RestController
@RequestMapping(value = "/api/v1/sqs/")
public class SQSController {
	
	@Autowired
	private SQSService sqsService;

	@PostMapping(value = "/")
	public void sendSQSMessage(@RequestBody String json) {
		sqsService.sendMessage(json);
	}
}
