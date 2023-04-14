package com.kinses38.exercise3;

import com.google.protobuf.DynamicMessage;
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
	private KafkaTemplate<String, StoreTransaction> kafkaTemplate;
	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class,args);
	}

	@Override
	public void run(String [] args){
		sendMessages();
	}

	public void sendMessages(){
		StoreTransaction storeTransaction = StoreTransaction.newBuilder()
				.setStoreName("Smyths")
				.setDate(1681423447) //don't judge me, I wanted to show int64
				.setInStore(InStore.newBuilder() //nested builder for deep objects
						.addAllItems(List.of("ps5", "controller", "hogwarts legacy")) //repeated fields give utility methods
						.addItems("AA batteries") // or we can add single items at a time.
						.setTotal(400)
						.setTransID(111))
				.build(); //only have to call build on the top level message.
		//TODO, make a Preorder transaction and send to kafka, remember it needs to be in the top StoreTransaction class for our producer to send it.
		// Well for at least this setup...hint hint, heads up.

		log.info("Preparing to send record \n{}", storeTransaction);

		ProducerRecord<String, StoreTransaction> record = new ProducerRecord<>("exercise3-topic", storeTransaction);
		kafkaTemplate.send(record);
	}

	@KafkaListener(
			topics = "exercise3-topic",
			groupId = "exercise3-group-id",
			containerFactory = "concurrentKafkaListenerContainerFactory"
	)
	public void listen(ConsumerRecord<String, DynamicMessage> consumerRecord){
		DynamicMessage dynamicMessage = consumerRecord.value();
		log.info("Hi from your completely oblivious consumer \n{}", dynamicMessage);
	}
}