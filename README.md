# light-rule
A rule engine or rule as a service based on Kotlin DSL

A long time ago, the rule engine was very popular. And then it fell out of fashion just like SOA. I think it is the time to rebrand the rule engine. Maybe we should call it micro-rule or light-rule in our light-platform. When I talking about the rule engine, I am not talking about the full-blown rule engine with an IDE, drag and drop on the UI to generate XML, etc. I am talking more at the level of a design pattern - a pattern of implementation where you want to have a kind of decoupling from the rule engine and rule itself that executed by the rule engine. Some type of generic infrastructure that you can plug into different services. In other words, a distributed rule engine that can collaborate between multiple services without copying data from each service. 

When adopting microservices architecture, the most important principal is decoupling. It requires that each service has its database and not copying data between services. In most of the cases, you only pass the ids between services/domains. This can ensure that each service can evolve independently without impact other services. 

It sounds very reasonable, but in practice a lot of questions are unanswered. In a monolithic application, it is easy to join multiple database tables to have a complicated query to get the required information; how can we query the data scattered in multiple microservices without database access? One solution is the adopt Event Sourcing and CQRS, but if you prefer synchronous services, you can embed the rule engine into each service and expose an interface to allow the caller to send rules for execution. The caller then aggregates the results to construct the information for the business decision. 

This approach is a paradigm shift as we are not collect data into a central location to apply the rules but send the rules to each individual service to execute. Instead of moving data around, we are move business logic around which is significantly smaller than data. 

Now we've discussed a generic idea, let's analyze two real use cases and see how the pattern is applied. 


Let's say we have an e-commerce site and we want to support customer specific pricing with the following rules. 

* If it is a long weekend, we give promotion of 10 percent off
* If the customer bought over $1000 last month, give him/her 2 percent off
* If the customer is living in a remote area, 5 percent off to offset the shipping cost.

As you can see, the information is scattered on three different services: promotion service, order service, and customer service. If these services can accept rules, then the caller can send the right rule or rules to the service to get the discount percentage of each and calculate the final price. 

Another example is fraud detection in a bank. Let's say we have the following rule to decide if a particular transaction is a fraud candidate or not. 

* If the shipping address is not the same as the customer's home address, add fraud score 50
* If the request IP address is outside of the country, then add fraud score 30. If in some blacklist countries, then 90
* If the customer only using this credit to buy grocery and suddenly bought something unusual, then add 40.

Again, the data that support these rules are scattered in three separate services. Instead of moving data together, we can send rules to each service and collect the result to make the final decision which might flag the transaction as potential fraud if the score is greater then 100. 

### Design




https://www.youtube.com/watch?v=Zm02wrli6uc

https://github.com/holgerbrandl/kscript
