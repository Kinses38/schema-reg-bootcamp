### Exercise 3

In resources, you'll find a proto file. This is the extension used for Google protocol buffers to describe a schema.  
There's nothing too complicated here. It describes a StoreTransaction that just has a Store Name and date of the transaction.  


But StoreTransactions can be one of two types, an in store transaction where someone has purchased any number of items, 
or it can be a preorder where you can optionally pay in advance for the items. 


1. This exercise used a maven protobuf plugin which I included in this exercises' pom. It's slightly different to the AVRO  
   version, instead of generating classes to our src folder it will be in our target/generated-sources dir.  
   So you're going to have to build them yourself. To do this run  
   ```mvn clean generate-sources```
2. You should see a few classes generated to target/generated-sources/protobuf now. And any errors in the Application should be resolved
3. Check out Core Application again, here you can see we're using a builder pattern to create a StoreTransaction.  
   GPB uses Builders to create Protobuf backed objects, and parsers to read serialized Protobuf messages.  
   We have access to not only StoreTransactions but InStore and Preorders.  
   They can be built independently of each other. Note that once a protobuf message is constructed it is immutable.
4. In terms of our Kakfa config, as you can probably guess we're using KafkaProto(De)Serializers now.  
   Also our producer related beans are now set to send StoreTransactions specifically, yet the Consumer related items are  
   "DynamicMessages", this is similar to the GeneratedRecord provided by AVRO, so you can deserialize any message once you have access to the schema.
5. Try running it now, fingers crossed all works. As usual check out the Kafka UI and take a look at the messages on the new topic.  
6. Now have a look at the schema, see that the entire schema is registered? This is a side effect of all the schemas being defined in the one class. And the schema name itself?  
   There's a new setting in our ProducerFactory, value name strategy, this decides how you register/reference schemas in your (de)serializer's and schema registry
7. Try creating a brand new PreOrder transaction as mentioned in CoreApplication, send it in addition to the Instore transaction.  
   It will have to be wrapped in a StoreTransaction.


### Bonus (Most likely will have to be in own time)
1. Set a breakpoint in the Listeners method, and inspect the incoming messages
2. Can see that it's not the actual StoreTransaction Type object we're receiving. Instead, it's the Generic DynamicMessage representation of it.
3. You can call "getAllFields()" or "getDescriptorForType" which will allow you to further inspect or manipulate the message but, we can see its not as easy as having access to the class.
4. In fact, GPB dev's recommend that you do not use the generated classes directly but instead wrap them in an application specific class to provide greater usability.
5. This is more a concern on the consumer side of things but just to make the point.

#### Additional
1. You could change the Producer related beans to instead send a record of type Message (protobuf one, not the spring one, watch your imports)
2. This means you could directly send InStore or PreOrder objects without the parent class StoreTransaction.
3. You may have to cast either transaction to a Message before you send.
4. This is feasible to do right now because a) we are the ones writing the messages, b) theres only two types.  
   but what if we were reading a serialized file with a number of different schema types and we don't necessarily care what they are?


   