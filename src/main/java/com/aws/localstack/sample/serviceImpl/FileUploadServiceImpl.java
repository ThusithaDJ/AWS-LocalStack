package com.aws.localstack.sample.serviceImpl;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.aws.localstack.sample.service.UploadService;

@Service
public class FileUploadServiceImpl implements UploadService {

	@Override
	public void uploadFile(String fileName, String filePath) {

		Regions clientRegion = Regions.AP_SOUTH_1;
		String bucketName = "localstack-bucket";

		try {
//          AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//                  .withRegion(clientRegion)
//                  .withCredentials(new ProfileCredentialsProvider())
//                  .build();

			String localstackRegion = "temp";
			String localstackS3EndpointURL = "http://localhost:4572";
			
			EndpointConfiguration endpointConfiguration = new EndpointConfiguration(localstackS3EndpointURL,
					localstackRegion);
			
			AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withEndpointConfiguration(endpointConfiguration)
					.enablePathStyleAccess().build();
			List<Bucket> listBuckets = s3Client.listBuckets();
			System.out.println(listBuckets);
			if(!s3Client.doesBucketExistV2(bucketName)) {
				s3Client.createBucket(bucketName);
			}
			TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3Client).build();

			// TransferManager processes all transfers asynchronously,
			// so this call returns immediately.
			Upload upload = tm.upload(bucketName, fileName, new File(filePath));
			System.out.println("Object upload started");

			// Optionally, wait for the upload to finish before continuing.
			upload.waitForCompletion();
			System.out.println("Object upload complete");
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		} catch (AmazonClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
