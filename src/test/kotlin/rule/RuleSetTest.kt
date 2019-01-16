package rule

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RuleSetTest {

    @Test
    fun `a simple set with two rules`() {
        val user = User(firstName = "Steve", lastName = "Hu", activated = false)
        val s = ruleSet<User> {
            id = "set001"
            description = "this is the first set of rules"
            rules {
                rule("com.networknt", "r0001", "1.0.0") {
                    name = "rule 0001"
                    description = "This is the first rule for testing with one condition and one action"
                    condition = {
                        it.firstName == "Steve" && it.lastName == "Hu"
                    }
                    action = {
                        println("Rule 1 is fired")
                    }
                }
                rule("com.networknt", "r0002", "1.0.0") {
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
    }
}