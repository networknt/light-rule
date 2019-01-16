package rule

class DefaultRuleEngine<T>: RuleEngine<T> {

    protected val ruleMap: MutableMap<String, Rule<T>> = mutableMapOf()
    protected val ruleSetMap: MutableMap<String, RuleSet<T>> = mutableMapOf()

    override fun addRule(rule: Rule<T>) {
        ruleMap.put(rule.id, rule)
    }

    override fun addRuleSet(ruleSet: RuleSet<T>) {
        ruleSetMap.put(ruleSet.setId, ruleSet)
    }

    override fun fireRule(id: String, t: T): Boolean {
        return ruleMap.get(id)?.fire(t) ?: false
    }

    override fun fireRuleSet(setId: String, t: T): Boolean {
        return ruleSetMap.get(setId)?.fire(t) ?: false
    }
}
