package rule

@DslMarker
annotation class RuleBuilder



class Rule private constructor(
        val host: String = "com.networknt",
        val id: String = "",
        val version: String = "1.0.0",
        val name: String?,
        var description: String?,
        val condition: (Any) -> Boolean,
        val action: () -> Unit
) {

    fun fire(fact: Any): Boolean {
        if (condition(fact)) {
            action()
            return true
        }
        return false
    }

    @RuleBuilder
    class Builder(val host: String, val id: String, val version: String) {
        var name: String? = null
        var description: String? = null
        var condition: (Any) -> Boolean = {false}
        var action: () -> Unit = {}

        fun setName(name: String?) = apply { this.name = name }
        fun setDescription(description: String?) = apply { this.description = description }
        fun setCondition(block: (Any) -> Boolean) = apply { this.condition = block}
        fun setAction(block: () -> Unit) = apply { this.action = block }
        fun build(): Rule {
            return Rule(host, id, version, name, description, condition, action)
        }
    }
}

fun rule(host: String, id: String, version: String, fn: Rule.Builder.() -> Unit): Rule {
    return Rule.Builder(host, id, version).apply(fn).build()
}
