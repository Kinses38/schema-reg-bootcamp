package com.kinses38.schemaregbootcamp;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@Slf4j
@SpringBootApplication
public class CoreApplication implements CommandLineRunner {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@Override
	public void run(String [] args) throws InterruptedException {
		sendMessages();
	}

	public void sendMessages(){
		List<String> listOfStrings = List.of("Message 1", "Message 2", "Message 3");
		for(String current : listOfStrings){
			ProducerRecord<String, String> record = new ProducerRecord<>("exercise1-topic", current);
			log.info("Sending {} to topic {}", current, record.topic());
			kafkaTemplate.send(record);
		}
	}

	@KafkaListener(
			containerFactory = "concurrentKafkaListenerContainerFactory",
			groupId = "exercise1",
			topics = "exercise1-topic"
	)
	public void listen(ConsumerRecord<String, String> consumerRecord){
		log.info("Received a message: {}", consumerRecord.value());
	}
}
