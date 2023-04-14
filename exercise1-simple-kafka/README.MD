# Exercise one
This is more of a "get everything up and running" task than anything actually heavy.
<br>In the top level of this project you'll see a docker-compose.yaml file

This will set you up with:
- 3 Kafka broker cluster running on ports 9092, 9093, 9094
- 1 zookeeper instance
- Schema registry running on port 8081
- Kafka ui, great diagnostics tool running on port 8085



1. Get your docker containers up <br> ```docker-compose up -d``` <br> Hopefully all runs well, and you can navigate to the [kafka ui](http://localhost:8085) running on localhost:8085
2. Have a look around the Spring Application in exercise1-simple-kafka <br> You'll see we have just a Core Application class and a Kakfa Config class. Nothing too fancy.
3. The Core Application implements CommandLine runner, this is just so we can trigger our producer to send kafka messages on startup.
4. It also has a Kafka Listener that listens to the same topic and attempts to print them as it receives them.
5. In the KafkaConfig class you can see we just set up a ProducerFactory, a KafkaTemplate that uses that factory. <BR> Then there's a ConsumerFactory that belongs to our ListenerContainerFactory. And a NewTopic bean that sets up our topic for us automatically.
6. The ProducerFactory is configured to Serialise String messages, both the values and keys.
7. The ConsumerFactory is configured to DeSerialize...
8. Go back to Core Application and attempt to run the class...
9. Hmmm, seems to be printing out a lot of errors. Lets stop the class and have a look at the exception..
10. Let's check if we actually managed to send anything, go back to Kafka UI and inspect your topic..
11. Appears to be messages there (should be anyway) So chances are its on the consumer side, lets double-check our config...  
    Whats the difference between the producer and consumer configs vs what we are actually attempting to send...
12. ??? Profit?