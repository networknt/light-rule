package rule

class Rule(val host: String, val id: String, val name: String, var description: String?, val condition: (Any) -> Boolean, val action: () -> Unit) {
    fun fire(fact: Any): Boolean {
        if (condition(fact)) {
            action()
            return true
        }
        return false
    }
}




fun main(args: Array<String>) {
    test1();
    test2();
}

fun test1(): Unit {
    val fact = "Kotlin"

    val condition: (Any) -> Boolean  = {
        if(it is String) it.startsWith("Kot") else false
    }

    val action: () -> Unit = {
        println("Rule 1 is fired")
    }

    val rule = Rule(
            "com.networknt",
            "rule0001",
            "Test rule 1",
            "This is the first rule for testing",
            condition,
            action)

    val result = rule.fire(fact)
    println(result)
}

data class Customer(val firstName: String, val lastName: String)

fun test2(): Unit {
    val fact = Customer("Steve", "Hu")

    val condition: (Any) -> Boolean  = {
        if(it is Customer) it.firstName == "Steve" && it.lastName == "Hu" else false
    }

    val action: () -> Unit = {
        println("Rule 2 is fired")
    }

    val rule = Rule(
            "com.networknt",
            "rule0001",
            "Test rule 1",
            "This is the first rule for testing",
            condition,
            action)

    val result = rule.fire(fact)
    println(result)
}
