package rule

/**
 * RuleEngine interface which is implemented by DefaultRuleEngine for rule and ruleSet registry and
 * lookup. Basically, it manages all the rules on the instance and can fire these rules based on the
 * setId of a rule set or id of a specific rule.
 *
 * Each RuleEngine instance will be associate with a fact that can be used to fire the rule(s).
 *
 * @author Steve Hu
 */
interface RuleEngine<T> {

    /**
     * Add a single rule to the rule engine which can be executed by the rule id.
     * @param rule Rule<T> the rule which is added to the single rule map.
     */
    fun addRule(rule: Rule<T>)

    /**
     * Add a ruleSet to the rule engine which can be executed by the setId.
     * @param ruleSet RuleSet<T> the ruleSet which is added to the ruleSet map
     */
    fun addRuleSet(ruleSet: RuleSet<T>)

    /**
     * fire a single rule by its id.
     * @param id rule Id
     * @param t T of fact
     * @return Boolean true if the rule condition is met and the action is executed.
     */
    fun fireRule(id: String, t: T): Boolean

    /**
     * fire a set of rules by its setId
     * @param setId rule set id
     * @param t T of fact
     * @return Boolean true if at least one rule condition is met.
     */
    fun fireRuleSet(setId: String, t: T): Boolean

    companion object {
        @Volatile private var INSTANCE: RuleEngine<*>? = null

        fun <T> getInstance(): RuleEngine<T> {
            INSTANCE?: synchronized(this) {
                DefaultRuleEngine<T>().also {
                    INSTANCE = it
                }
            }
            return INSTANCE as RuleEngine<T>
        }
    }
}
