package com.br.modulate.common.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.util.json.Jackson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AmazonSqsService {

    @Autowired
    private AmazonSQS amazonSQS;

    @Retryable(
            value = Exception.class,
            maxAttempts = 3, backoff = @Backoff(5000))
    public void sendMessage(final String destination, final Object object) {

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(destination)
                .withMessageBody(Jackson.toJsonString(object));


        try {
            sendSQSMessage(sendMessageRequest);
        } catch (Exception e) {
//            log.error(String.format("ERROR sending to AWS SQS %s",
//                    sendMessageRequest.getQueueUrl()));
            throw new RuntimeException(e.getMessage());
        }
    }

    @Retryable(
            value = Exception.class,
            maxAttempts = 3, backoff = @Backoff(5000))
    public void sendMessageWithDelay(final String destination, final Object object) {
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withDelaySeconds(900)
                .withQueueUrl(destination)
                .withMessageBody(Jackson.toJsonString(object));

        try {
            sendSQSMessage(sendMessageRequest);
        } catch (Exception e) {
//            log.error(String.format("ERROR sending to AWS SQS %s",
//                    sendMessageRequest.getQueueUrl()));
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async("registrationExecutor")
    private void sendSQSMessage(final SendMessageRequest sendMessageRequest) throws Exception {

        try {
            amazonSQS.sendMessage(sendMessageRequest);
//            log.info(String.format("Sent to AWS SQS %s - dada=%s",
//                    sendMessageRequest.getQueueUrl(),
//                    sendMessageRequest.toString()));
        } catch (Exception e) {
//            log.error(String.format("ERROR sending to AWS SQS %s - dada=%s , Error message=%s",
//                    sendMessageRequest.getQueueUrl(),
//                    sendMessageRequest.toString(),
//                    e.getMessage()));
            throw e;
        }
    }
}
