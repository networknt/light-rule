package rule

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test


class ConditionTest {

    val matchHello: (String) -> Boolean = { it == "hello" }


    @Test
    fun `negate not operator`() {
        assertTrue(matchHello("hello"))
        assertFalse(!matchHello("hello"))
    }

    @Test
    fun `negate operator in map filter`() {
        mapOf("hello" to "world", "hi" to "everyone")
                .filterKeys(!matchHello)
                .forEach(::println)
    }

    @Test
    fun `predicate compisition`() {
        val customer = Customer("Steve", "Hu")
        val p1: Predicate<Customer> = { it.firstName == "Steve"}
        val p2: Predicate<Customer> = { it.lastName == "Hu"}
        val p: Boolean = p1.invoke(customer) && p2.invoke(customer);

        println("p = $p")

        val anotherCustomer = Customer("John", "Doe")
        val pa = p1.invoke(anotherCustomer) && p2.invoke(anotherCustomer);

        println("pa = $pa")

        val pb = p1(anotherCustomer) && p2(anotherCustomer)

        println("pb = $pb")

    }

    @Test
    fun `operator or compond`() {
        val customer = Customer("Steve", "Hu")
        val c1: Condition<Customer> = Condition{ it.firstName == "Steve"}
        val c2: Condition<Customer> = Condition{ it.lastName == "Doe"}
        var cs1 = c1.or(c2)
        println(cs1.invoke(customer))

        var cs2 = Condition(hasLastName("Doe")) or Condition(hasFirstName("John"))
        println(cs2.invoke(customer))
    }

    @Test
    fun `operator and compond`() {
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
    }

    @Test
    fun `user with or and compbined`() {
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
    }

    @Test
    fun `user with and or compbined`() {
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
    }

}


data class Customer(val firstName: String, val lastName: String)

fun hasLastName(lastName: String): Predicate<Customer> = {it.lastName.equals(lastName)}
fun hasFirstName(firstName: String): Predicate<Customer> = {it.firstName.equals(firstName)}

