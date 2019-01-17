# light-rule
A rule engine or rule as a service based on Kotlin DSL

A long time ago, the rule engine was very popular. And then it fell out of fashion just like SOA. I think it is the time to rebrand the rule engine. Maybe we should call it micro-rule or light-rule in our light-platform. When I am talking about the rule engine, I am not talking about the full-blown rule engine with an IDE, drag and drop on the UI to generate XML, etc. I am talking more at the level of a design pattern - a pattern of implementation where you want to have a kind of decoupling from the rule engine and rule itself that executed by the rule engine which is a type of generic infrastructure that you can plug into different services. In other words, a distributed rule engine that can collaborate between multiple services without copying data from each service. 

When adopting microservices architecture, the most essential principle is decoupling. It requires that each service has its database and not copying data between services. In most of the cases, you only pass the ids between services/domains. This can ensure that each service can evolve independently without impact other services. 

It sounds very reasonable, but in practice a lot of questions are unanswered. In a monolithic application, it is easy to join multiple database tables to have a complicated query to get the required information; how can we query the data scattered in multiple microservices without database access? One solution is to adopt Event Sourcing and CQRS, but if you prefer synchronous services, you can embed the rule engine into each service and expose an interface to allow the caller to fire rules for execution. The caller then aggregates the results to construct the information for the business decision. 

This approach is a paradigm shift as we are not collect data into a central location to apply the rules but send/fire the rules to each service to execute. Instead of moving data around, we are move business logic around which is significantly smaller than data. 

Now we've discussed a generic idea, let's analyze two real use cases and see how the pattern is applied. 

Let's say we have an e-commerce site and we want to support customer specific pricing with the following rules. 

* If it is a long weekend, we give promotion of 10 percent off
* If the customer bought over $1000 last month, give him/her 2 percent off
* If the customer is living in a remote area, 5 percent off to offset the shipping cost.

As you can see, the information is scattered on three different services: promotion service, order service, and customer service. If these services can accept rules or they have rules embedded, then the caller can send/fire the right rule or rules to the service to get the discount percentage of each and calculate the final price. 

Another example is fraud detection in a bank. Let's say we have the following rule to decide if a particular transaction is a fraud candidate or not. 

* If the shipping address is not the same as the customer's home address, add fraud score 50
* If the request IP address is outside of the country, then add fraud score 30. If in some blacklist countries, then 90
* If the customer only using this credit to buy grocery and suddenly bought something unusual, then add 40.

Again, the data that support these rules are scattered in three separate services. Instead of moving data together, we can send/fire rules to each service and collect the result to make the final decision which might flag the transaction as potential fraud if the score is higher then 100. 

### Design

One of the primary reasons the traditional rule engines are abandoned is due to the performance. These rule engines define the rules in another language that can be understood and modified by business people. Then the rule engines need to interpret the rules for condition evaluation and action execution. They need a lot of resources to execute the rules and the latency cannot meet the SLA of online applications. 

The light-rule is based on Kotlin DSL which is syntax sugar on top of the general programming language Kotlin. The domain-specific language is not a completely new programming language with a brand new syntax. We just set up a particular way of using the Kotlin language. 

Here is a list of advantages of Kotlin DSL.

* Proper IDE support: auto-completion, refactoring, navigation to the source, etc. This is possible due to Kotlin being statically-typed language
* Automatic detection of imports
* Utilizing cool Kotlin features like extension methods, first-class functions
* Using a familiar syntax
* Native performance of Java/Kotlin for the rule execution


### Usage

There are two ways to use the light-rule. 

* Embedded the light-rule into your application
* Deploy a light-4j microservices with rules and rule engine


##### Rule Engine

The RuleEngine<T> is an interface, and there is a singleton DefaultRuleEngine<T> implementation. It is responsible for managing standalone rules or rule set which groups a list of rules. 

Here is the interface

```
interface RuleEngine<T> {

    /**
     * Add a single rule to the rule engine which can be executed by the rule id.
     * @param rule Rule<T> the rule which is added to the single rule map.
     */
    fun addRule(rule: Rule<T>)

    /**
     * Add a ruleSet to the rule engine which can be executed by the setId.
     * @param ruleSet RuleSet<T> the ruleSet which is added to the ruleSet map
     */
    fun addRuleSet(ruleSet: RuleSet<T>)

    /**
     * fire a single rule by its id.
     * @param id rule Id
     * @param t T of fact
     * @return Boolean true if the rule condition is met and the action is executed.
     */
    fun fireRule(id: String, t: T): Boolean

    /**
     * fire a set of rules by its setId
     * @param setId rule set id
     * @param t T of fact
     * @return Boolean true if at least one rule condition is met.
     */
    fun fireRuleSet(setId: String, t: T): Boolean

    companion object {
        @Volatile private var INSTANCE: RuleEngine<*>? = null

        fun <T> getInstance(): RuleEngine<T> {
            INSTANCE?: synchronized(this) {
                DefaultRuleEngine<T>().also {
                    INSTANCE = it
                }
            }
            return INSTANCE as RuleEngine<T>
        }
    }
}

```

##### Rule

Rule DSL looks like the following. 

```
        val rule = rule<User>("com.networknt.r0002-1.0.0") {
            name = "rule 0002"
            description = "This is the rule for testing with multiple conditions compound with or"
            condition = or (
                    withFirstName("Steve"),
                    withLastName("Hu"),
                    withGender(Gender.MALE)
                )
            action = {
                println("Rule 2 is fired")
                println("Second action is executed")
            }

        }
        val result = rule.fire(User(firstName = "Steve", lastName = "Hu"))
        assertTrue(result)
```

##### RuleSet

RuleSet DSL looks like the following.

```
        val user = User(firstName = "Steve", lastName = "Hu", activated = false)
        val s = ruleSet<User> {
            setId = "set001"
            description = "this is the first set of rules"
            rules {
                rule("com.networknt.r0001-1.0.0") {
                    name = "rule 0001"
                    description = "This is the first rule for testing with one condition and one action"
                    condition = {
                        it.firstName == "Steve" && it.lastName == "Hu"
                    }
                    action = {
                        println("Rule 1 is fired")
                    }
                }
                rule("com.networknt.r0002-1.0.0") {
                    name = "rule 0002"
                    description = "This is the second rule for testing with one condition and one action"
                    condition = or (
                            withFirstName("Steve"),
                            withLastName("Hu"),
                            withGender(Gender.MALE)
                    )
                    action = {
                        println("Rule 2 is fired")
                    }
                }
            }
        }
        assertTrue(s.fire(user))

```

##### Condition

For a rule engine, the most important thing is how to write the conditions. In the light-rule, we are leveraging the Kotlin DSL to the full potential. In most of the cases, you can define the conditon with Kotlin Lambdas. 

For example.

```
        val p1: Predicate<Customer> = { it.firstName == "Steve"}
        val p2: Predicate<Customer> = { it.lastName == "Hu"}
        val p: Boolean = p1.invoke(customer) && p2.invoke(customer);

```

You can also wrapped Predicate<T> with Condition and make CompoundCondition with `and` or `or`. 

```
        val customer = Customer("Steve", "Hu")
        val c1: Condition<Customer> = Condition{ it.firstName == "Steve"}
        val c2: Condition<Customer> = Condition{ it.lastName == "Doe"}
        var cs1 = c1.or(c2)
        println(cs1.invoke(customer))

```

Or

```
        var cs2 = Condition(hasLastName("Doe")) or Condition(hasFirstName("John"))
        println(cs2.invoke(customer))

```

Or

```
        val customer = Customer("Steve", "Hu")
        val c1: Condition<Customer> = Condition{ it.firstName == "Steve"}
        val c2: Condition<Customer> = Condition{ it.lastName == "Doe"}
        val c3: Condition<Customer> = Condition{ it.lastName == "Hu"}
        var cs1 = c1.and(c2)
        assertFalse(cs1.invoke(customer))
        var cs2 = c1.and(c3)
        assertTrue(cs2.invoke(customer))

        var cs3 = c1 and c3
        assertTrue(cs3.invoke(customer))

        var cs4 = Condition(hasLastName("Hu")) and Condition(hasFirstName("Steve"))
        assertTrue(cs4.invoke(customer))

```

##### CompondCondition

You can define complicated rules like below. 

```
        val user1 = User(firstName = "Steve", lastName = "Hu", gender = Gender.MALE, activated = false)
        val user2 = User(firstName = "John", lastName = "Doe", gender = Gender.MALE, activated = true)

        var rule =
                or (
                    and (
                       withFirstName("Steve"),
                       withLastName("Hu"),
                       withGender(Gender.MALE)
                    ),
                    and(
                        withActivated(true)
                    )
                )
        assertTrue(rule.invoke(user1))
        assertTrue(rule.invoke(user2))

```

Or

```
        val user1 = User(firstName = "Steve", lastName = "Hu", gender = Gender.MALE, activated = false)
        val user2 = User(firstName = "John", lastName = "Doe", gender = Gender.MALE, activated = true)

        var rule =
                and (
                        or (
                                withFirstName("Steve"),
                                withLastName("Hu"),
                                withGender(Gender.MALE)
                        ),
                        or (
                                withActivated(true)
                        )
                )
        assertFalse(rule.invoke(user1))
        assertTrue(rule.invoke(user2))

```

