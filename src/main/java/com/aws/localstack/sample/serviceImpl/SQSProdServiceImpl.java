package com.aws.localstack.sample.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.aws.localstack.sample.service.SQSService;

@ConditionalOnProperty(name = "aws.debug.localstack", havingValue = "false", matchIfMissing = false)
@Service
public class SQSProdServiceImpl implements SQSService {

	private static final Logger LOG = LoggerFactory.getLogger(SQSProdServiceImpl.class);

	@Value("${aws.sqs.queuename}")
	private String sqsQueueName;

	@Value("${aws.credentials.profile:default}")
	private String awsCredentialProfile;

	@Override
	public String sendMessage(String json) {
		LOG.info("SQS PROD Service");

		final AmazonSQS sqs = AmazonSQSClientBuilder.standard()
				.withCredentials(new ProfileCredentialsProvider(awsCredentialProfile)).withRegion(Regions.US_EAST_1)
				.build();

		String queueUrl = sqs.getQueueUrl(sqsQueueName).getQueueUrl();

		SendMessageRequest send_msg_request = new SendMessageRequest().withQueueUrl(queueUrl).withMessageBody(json)
				.withDelaySeconds(5);

		return sqs.sendMessage(send_msg_request).getMessageId();

	}

}
