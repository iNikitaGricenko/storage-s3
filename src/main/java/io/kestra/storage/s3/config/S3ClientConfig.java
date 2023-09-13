package io.kestra.storage.s3.config;

import com.amazonaws.auth.*;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Factory
@Singleton
@S3StorageEnabled
@RequiredArgsConstructor
public class S3ClientConfig {

	private final S3Properties s3Properties;

	@Bean
	public AmazonS3 getAmazonS3() {
		AmazonS3ClientBuilder clientBuilder = AmazonS3ClientBuilder.standard();

		if (s3Properties.getEndpoint() != null && s3Properties.getRegion() != null) {
			AwsClientBuilder.EndpointConfiguration endpointConfiguration =
					getAWSEndpointConfiguration(s3Properties.getEndpoint(), s3Properties.getRegion());

			clientBuilder.withEndpointConfiguration(endpointConfiguration);
		}

		return clientBuilder.withCredentials(getCredentials()).build();
	}

	private AwsClientBuilder.EndpointConfiguration getAWSEndpointConfiguration(String awsEndpoint, String awsRegion) {
		return new AwsClientBuilder.EndpointConfiguration(awsEndpoint, awsRegion);
	}

	private AWSCredentialsProvider getCredentials() {
		if (s3Properties.getAccessKey() != null && s3Properties.getSecretKey() != null) {
			AWSCredentials credentials = new BasicAWSCredentials(s3Properties.getAccessKey(), s3Properties.getSecretKey());
			return new AWSStaticCredentialsProvider(credentials);
		}
		return new DefaultAWSCredentialsProviderChain();
	}

}
