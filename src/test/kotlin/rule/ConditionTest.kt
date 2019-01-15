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

        println(p)

        val anotherCustomer = Customer("John", "Doe")
        val pa = p1.invoke(anotherCustomer) && p2.invoke(anotherCustomer);

        println(pa)

        val pb = p1(anotherCustomer) && p2(anotherCustomer)

        println(pb)

    }

    @Test
    fun `operator or compond`() {
        val customer = Customer("Steve", "Hu")
        val p1: Predicate<Customer> = { it.firstName == "Steve"}
        val p2: Predicate<Customer> = { it.lastName == "Doe"}
        var cs = p1.or(p2)
        println(cs.evaluate(customer))

        var cs1 = hasLastName("Doe") or hasFirstName("John")
        println(cs1.evaluate(customer))

    }

    @Test
    fun `operator and compond`() {
        val customer = Customer("Steve", "Hu")
        val p1: Predicate<Customer> = { it.firstName == "Steve"}
        val p2: Predicate<Customer> = { it.lastName == "Doe"}
        val p3: Predicate<Customer> = { it.lastName == "Hu"}
        var cs1 = p1.and(p2)
        println(cs1.evaluate(customer))
        var cs2 = p1.and(p3)
        println(cs2.evaluate(customer))

        var cs3 = p1 and p3
        println(cs3.evaluate(customer))

        var cs4 = ( hasLastName("Hu") and hasFirstName("Steve"))
        println(cs4.evaluate(customer))

    }

}


data class Customer(val firstName: String, val lastName: String)

fun hasLastName(lastName: String): Predicate<Customer> = {it.lastName.equals(lastName)}
fun hasFirstName(firstName: String): Predicate<Customer> = {it.firstName.equals(firstName)}

