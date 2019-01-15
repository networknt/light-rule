package rule

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RuleTest {

    @Test
    fun `a simple rule with string fact`() {
        val fact = "Kotlin"

        val condition1: Predicate<String> = {
            it.startsWith("Kot")
        }

        val action1: () -> Unit = {
            println("Rule 1 is fired")
        }

        val rule = rule<String>("com.networknt", "rule0001", "1.2.0") {
            name = "rule 1"
            description = "This is the first rule for testing"
            condition = condition1
            action = action1
        }

        val result = rule.fire(fact)
        assertTrue(result)
        println(result)
    }

    @Test
    fun `a simple rule with one condition and one action`() {
        val rule = rule<User>("com.networknt", "r0001", "1.0.0") {
            name = "rule 0001"
            description = "This is the first rule for testing with one condition and one action"
            condition = {
                it.firstName == "Steve" && it.lastName == "Hu"
            }
            action = {
                println("Rule 1 is fired")
                println("Second action is executed")
            }

        }
        val result = rule.fire(User(firstName = "Steve", lastName = "Hu"))
        println(result)
    }

    @Test
    fun `a rule with or conditions`() {
        val rule = rule<User>("com.networknt", "r0002", "1.0.0") {
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
        println(result)

    }

    @Test
    fun `a rule with and conditions`() {
        val rule = rule<User>("com.networknt", "r0003", "1.0.0") {
            name = "rule 0003"
            description = "This is the rule for testing with multiple conditions compound with and"
            condition = and (
                    withFirstName("Steve"),
                    withLastName("Hu"),
                    withGender(Gender.MALE),
                    withActivated(true)  // this condition fails.
            )
            action = {
                println("Rule 3 is fired")
                println("Second action is executed")
            }
        }
        val result = rule.fire(User(firstName = "Steve", lastName = "Hu"))
        assertFalse(result)
        println(result)
    }

}
