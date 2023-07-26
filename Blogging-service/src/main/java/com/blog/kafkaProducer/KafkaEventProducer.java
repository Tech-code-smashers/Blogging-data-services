package com.blog.kafkaProducer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaEventProducer {
    public static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventProducer.class);
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    private ProducerRecord<String,String> buildProduceRecord(String topicName, String key, String value, Map<String,String>headers){
        List<Header> headerData = new ArrayList<>();
        for(Map.Entry<String,String> header:headers.entrySet()){
            headerData.add(new RecordHeader(header.getKey(),header.getValue().getBytes()));
        }
        return new ProducerRecord<>(topicName,null,key,value,headerData);
    }

    @Async
    public SendResult<String,String> sendProducerEventSync(String topic, String producerEvent, String key,
                                                           Map<String, String> headers) throws ExecutionException, InterruptedException {
        SendResult<String, String> sendResult;
        try {
            ProducerRecord<String, String> producerRecord = buildProduceRecord(topic, key, producerEvent, headers);
            sendResult = kafkaTemplate.send(producerRecord).get();
            LOGGER.info("Message Sent Successfully for the key = {}, and the value is = {}, partition is = {} ", key,
                    producerEvent, sendResult.getRecordMetadata().partition());
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Error Sending Message key = {}, and the exception is = {}", key, e.getMessage());
            throw e;
        }
        return sendResult;
    }
}
