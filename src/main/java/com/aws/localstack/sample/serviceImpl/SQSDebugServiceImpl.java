package com.aws.localstack.sample.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.aws.localstack.sample.service.SQSService;

@ConditionalOnProperty(name = "aws.debug.localstack", havingValue = "true", matchIfMissing = true)
@Service
public class SQSDebugServiceImpl implements SQSService {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(SQSDebugServiceImpl.class);


	@Value("${aws.debug.localstack.region:temp}")
	private String localstackRegion;
	
	@Value("${aws.debug.localstack.endpoint}")
	private String localstackSQSEndpointURL;
	
	@Value("${aws.sqs.queuename}")
	private String sqsQueueName;
	
	@Value("${aws.credentials.profile:default}")
	private String awsCredentialProfile;
	
	
	@Override
	public void sendMessage(String json) {
		LOG.info("SQS Debug Service");

		EndpointConfiguration endpointConfiguration = new EndpointConfiguration(localstackSQSEndpointURL,
				localstackRegion);

		final AmazonSQS sqs = AmazonSQSClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).withCredentials(new ProfileCredentialsProvider(awsCredentialProfile))
				.build();

		String queueUrl = sqs.getQueueUrl(sqsQueueName).getQueueUrl();

		SendMessageRequest send_msg_request = new SendMessageRequest()
				.withQueueUrl(queueUrl).withMessageBody(json).withDelaySeconds(5);

		sqs.sendMessage(send_msg_request);
	}

}
