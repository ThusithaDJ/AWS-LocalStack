package com.aws.localstack.sample.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.aws.localstack.sample.service.SNSService;

@ConditionalOnProperty(name = "aws.sns.debug", havingValue = "false", matchIfMissing = false)
@Service
public class SNSProdServiceImpl implements SNSService {

	
	private static final Logger LOG = LoggerFactory.getLogger(SNSProdServiceImpl.class);

	@Value("${aws.credentials.profile:default}")
	private String awsCredentialProfile;
	
	@Value("${aws.sns.topic.arn}")
	private String awsSNSTopicARN;
	
	@Value("${aws.sns.region}")
	private String awsSNSRegion;
	
	@Override
	public String publishMessage(String message) {
		LOG.info("SNS PROD Service");
		try {
			final AmazonSNS sns = AmazonSNSClientBuilder.standard()
					.withCredentials(new ProfileCredentialsProvider(awsCredentialProfile)).withRegion(awsSNSRegion)
					.build();
			
			final PublishRequest publishRequest = new PublishRequest(awsSNSTopicARN, message);
			final PublishResult publishResponse = sns.publish(publishRequest);
			return publishResponse.getMessageId();
		} catch (Exception e) {
			return e.getMessage();
		}
		
	}

}
