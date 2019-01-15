package rule

fun main(args: Array<String>) {
    test1();
    test2();
}

fun test1(): Unit {
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
    println(result)
}

data class Customer(val firstName: String, val lastName: String)

fun test2(): Unit {
    val fact = Customer("Steve", "Hu")

    val rule = rule<Customer>("net.lightapi", "rule0002", "1.0.0") {
        name = "rule 1"
        description = "This is the first rule for testing"
        condition = {
            it.firstName == "Steve" && it.lastName == "Hu"
        }
        action = {
            println("Rule 2 is fired")
            println("Second action is executed")
        }
    }

    val result = rule.fire(fact)
    println(result)
}
