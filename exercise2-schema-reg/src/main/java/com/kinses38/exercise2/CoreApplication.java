package com.kinses38.exercise2;

import com.kinses38.schema.Person;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@SpringBootApplication
public class CoreApplication implements CommandLineRunner {

	@Autowired
	private KafkaTemplate<String, Person> kafkaTemplate;

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@Override
	public void run(String [] args) {
		sendMessages();
	}

	public void sendMessages() {
		//If I use an avro generated class, I can just create it like a pojo... the class provides the functionality for the serializer to go from pojo -> avro -> bytes.
		Person person = new Person("Shaun", 35, "caffeinated");

//		Can also build an avro backed pojo like so
//		Person person2 = Person.newBuilder().setName("John Doe").setAge(20).setBloodtype("A-").build();


		ProducerRecord<String, Person> record = new ProducerRecord<>("exercise2-topic", person);
		log.info("Sending record {} to topic {}", record, record.topic());
		kafkaTemplate.send(record);
		log.info("Record sent, we should prob check the kafka ui for the topic and schema..");
	}


	@KafkaListener(
			containerFactory = "concurrentKafkaListenerContainerFactory",
			groupId = "exercise2",
			topics = "exercise2-topic"
	)
	public void listen(ConsumerRecord<String, GenericRecord> consumerRecord){
		//TODO notice we arent using the class? Pretend this is in another microservice and we didnt have access to it.
		log.info("Received a message: {}", consumerRecord.value());

		GenericRecord record = consumerRecord.value();
		log.info("Wonder can we get the name? {}", record.get("name"));
		//so as consumers we can interact with this record just like a hashmap. You can set your deserializer to use
		// a specific avro schema to deserialize, so you can work with the class directly. But you have to override and write it yourself..
	}
}