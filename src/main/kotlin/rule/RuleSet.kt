package rule

/**
 * This class represents a set of rules in a group. They must be working with a same instance of T. All actions
 * will be executed if rules' conditions are true. This is good use case for request validation. You might have
 * a set of validation rules and return all the validation error when executing the entire set of rules. Another
 * use case might be response filter for a service in the fine-grained authorization. Each rule will apply a filter
 * on either a column or a row depending on if the conditions are met.
 *
 * @author Steve Hu
 */
class RuleSet<T>(val setId: String, val description: String, val rules: List<Rule<T>>) {
    /**
     * Fire a set of rules. Return true is at least one rule condition is true.
     */
    fun fire(t: T): Boolean {
        var result : Boolean = false;
        rules.forEach() {
            var r = it.fire(t)
            result = result || r
        }
        return result
    }
}

class RuleSetBuilder<T> {
    var setId: String = ""
    var description: String = ""
    var rules = mutableListOf<Rule<T>>()

    fun rules(block: RULES<T>.() -> Unit) {
        rules.addAll(RULES<T>().apply(block))
    }
    fun build(): RuleSet<T> = RuleSet<T>(setId, description, rules)
}

class RULES<T>: ArrayList<Rule<T>>() {
    fun rule(id: String, block: Rule.Builder<T>.() -> Unit) {
        add(Rule.Builder<T>(id).apply(block).build())
    }
}

fun <T> ruleSet(block: RuleSetBuilder<T>.() -> Unit): RuleSet<T> = RuleSetBuilder<T>().apply(block).build()

