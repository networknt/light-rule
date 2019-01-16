package rule

@DslMarker
annotation class RuleMarker

class Rule<T> constructor(
        val host: String = "com.networknt",
        val id: String = "",
        val version: String = "1.0.0",
        val name: String?,
        var description: String?,
        val condition: Predicate<T>,
        val action: () -> Unit
) {

    fun fire(t: T): Boolean {
        if (condition(t)) {
            action()
            return true
        }
        return false
    }

    @RuleMarker
    class Builder<T>(val host: String, val id: String, val version: String) {
        var name: String? = null
        var description: String? = null
        var condition: Predicate<T> = Condition<T>{false}
        var action: () -> Unit = {}

        fun setName(name: String?) = apply { this.name = name }
        fun setDescription(description: String?) = apply { this.description = description }
        fun setCondition(block: Predicate<T>) = apply { this.condition = block}
        fun setAction(block: () -> Unit) = apply { this.action = block }
        fun build(): Rule<T> {
            return Rule(host, id, version, name, description, condition, action)
        }
    }
}

fun <T> rule(host: String, id: String, version: String, fn: Rule.Builder<T>.() -> Unit): Rule<T> {
    return Rule.Builder<T>(host, id, version).apply(fn).build()
}
