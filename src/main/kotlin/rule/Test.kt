package rule

fun main(args: Array<String>) {
    test1();
    test2();
}

fun test1(): Unit {
    val fact = "Kotlin"

    val condition1: (Any) -> Boolean  = {
        if(it is String) it.startsWith("Kot") else false
    }

    val action1: () -> Unit = {
        println("Rule 1 is fired")
    }

    val rule = rule("com.networknt", "rule0001", "1.2.0") {
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

    val rule = rule("net.lightapi", "rule0002", "1.0.0") {
        name = "rule 1"
        description = "This is the first rule for testing"
        condition = {
            if(it is Customer) it.firstName == "Steve" && it.lastName == "Hu" else false
        }
        action = {
            println("Rule 2 is fired")
            println("Second action is executed")
        }
    }

    val result = rule.fire(fact)
    println(result)
}
