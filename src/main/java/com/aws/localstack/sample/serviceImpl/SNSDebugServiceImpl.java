package com.aws.localstack.sample.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.aws.localstack.sample.service.SNSService;

@ConditionalOnProperty(name = "aws.sns.debug", havingValue = "true", matchIfMissing = true)
@Service
public class SNSDebugServiceImpl implements SNSService {

	private static final Logger LOG = LoggerFactory.getLogger(SNSDebugServiceImpl.class);

	@Value("${aws.credentials.profile:default}")
	private String awsCredentialProfile;

	@Value("${aws.sns.region}")
	private String awsSNSRegion;

	@Value("${aws.sns.localstack.endpoint}")
	private String awsSNSLocalstackEndpoint;

	@Value("${aws.sns.topic.arn}")
	private String awsSNSTopicARN;

	@Override
	public String publishMessage(String message) {
		LOG.info("SNS Debug Service");

		try {
			EndpointConfiguration endpointConfiguration = new EndpointConfiguration(awsSNSLocalstackEndpoint, awsSNSRegion);

			final AmazonSNS sns = AmazonSNSClientBuilder.standard().withEndpointConfiguration(endpointConfiguration)
					.withCredentials(new ProfileCredentialsProvider(awsCredentialProfile)).build();

			final PublishRequest publishRequest = new PublishRequest(awsSNSTopicARN, message);
			final PublishResult publishResponse = sns.publish(publishRequest);
			return publishResponse.getMessageId();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
