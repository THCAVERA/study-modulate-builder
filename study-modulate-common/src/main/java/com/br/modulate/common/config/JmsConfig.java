package com.br.modulate.common.config;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Configuration
@EnableJms
public class JmsConfig {

    @Value("${sqs.region}")
    private String amazonAWSRegion;
    @Value("${sqs.key}")
    private String key;
    @Value("${sqs.secret}")
    private String secret;

    @Autowired
    private AWSCredentials awsCredentials;

    private AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        final SQSConnectionFactory sqsConnectionFactory = SQSConnectionFactory.builder()
                .withRegion(Region.getRegion(Regions.fromName(amazonAWSRegion)))
                .withAWSCredentialsProvider(amazonAWSCredentialsProvider())
                .build();
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(sqsConnectionFactory);
        factory.setDestinationResolver(new DynamicDestinationResolver());
        factory.setErrorHandler(throwable -> {});
        factory.setConcurrency("3-10");
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        return factory;
    }


    @Bean
    public JmsTemplate defaultJmsTemplate() {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxErrorRetry(1);
        clientConfiguration.setConnectionTimeout(500); // 500 milliseconds
        clientConfiguration.setSocketTimeout(1000); // 1 second
        clientConfiguration.setRequestTimeout(500); // 500 milliseconds
        clientConfiguration.setClientExecutionTimeout(3000); // 3 seconds
        final SQSConnectionFactory sqsConnectionFactory = SQSConnectionFactory.builder()
                .withRegion(Region.getRegion(Regions.fromName(amazonAWSRegion)))
                .withAWSCredentialsProvider(amazonAWSCredentialsProvider())
                .withClientConfiguration(clientConfiguration)
                .build();
        return new JmsTemplate(sqsConnectionFactory);
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(key, secret);
    }

    @Bean(name = "sqsClient")
    public AmazonSQS getSQSClient() {

        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxErrorRetry(1);
        clientConfiguration.setConnectionTimeout(100000); // 1 seconds
        clientConfiguration.setSocketTimeout(200000); // 1 second
        clientConfiguration.setRequestTimeout(100000); // 1 seconds
        clientConfiguration.setClientExecutionTimeout(300000); // 3 seconds

        AwsClientBuilder<AmazonSQSClientBuilder, AmazonSQS> builder = AmazonSQSClientBuilder.standard();
        builder.withRegion(Regions.fromName(amazonAWSRegion));
        builder.withCredentials(amazonAWSCredentialsProvider());
        builder.setClientConfiguration(clientConfiguration);
        return builder.build();
    }
}
