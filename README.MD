Nothing too fancy here.
Just 3 exercises to get people comfortable interacting with Schema Registry and Kafka Serializer/DeSerializers

In this level you'll find a docker-compose file that will provide you with:
- 3 Kafka broker cluster running on ports 9092, 9093, 9094
- 1 zookeeper instance
- Schema registry running on port 8081
- Kafka ui, great diagnostics tool running on port 8085

Each exercise comes with its own pom for specific dependencies required.
The parent pom contains common dependencies.
Each exercise consists of a CoreApplication class that will run the examples. There's also a readme in all exercise modules describing what you need to do or observe.  
You'll need the docker containers running for all exercises. None of the containers are persistent so if you restart them they'll be wiped.

1. Exercise 1: fixing serializer/deserializer mis-configurations
2. Exercise 2: Sending specific Avro records and listening for GenericRecords. Intro to Schema Reg
3. Exercise 3: Protobuf messages and sending multiple types per topic

