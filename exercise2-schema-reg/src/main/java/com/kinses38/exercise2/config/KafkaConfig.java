package com.kinses38.exercise2.config;

import com.kinses38.schema.Person;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

	@Value("${spring.kafka.bootstrap-servers}")
	private String bootstrapServer;


	//I'm set up to send a Person Pojo. But really I'm a class generated by AVRO.
	@Bean
	public ProducerFactory<String, Person> producerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		//NonSpecific avro serializer
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

		config.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

		return new DefaultKafkaProducerFactory<>(config);
	}

	@Bean
	public KafkaTemplate<String, Person> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory(), false);
	}

	//I'm set up to read GenericRecords, this is basically way to read Avro messages without knowing the backing class.
	@Bean
	public ConsumerFactory<String, GenericRecord> consumerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		//NonSpecific avro deserializer
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
		//let the serializer know where schema registry is, if you ctrl+click the KafkaAvroDeserializer above you'll see it takes a schema reg client as an arg;
		config.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

		return new DefaultKafkaConsumerFactory<>(config);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, GenericRecord> concurrentKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, GenericRecord> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(consumerFactory());

		return containerFactory;
	}

	@Bean
	public NewTopic topic() {
		return TopicBuilder.name("exercise2-topic").replicas(3).build();
	}
}
