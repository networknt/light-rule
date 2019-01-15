package rule

class CompondCondition<T> : Predicate<T> {
    val operator: CompondOperator
    val conditions: List<Condition<T>>

    constructor(operator: CompondOperator, conditions: List<Condition<T>>) {
        this.operator = operator
        this.conditions = conditions
    }


    override fun invoke(t: T): Boolean {
        if(operator == CompondOperator.AND) {
            // all predicates must be true to return true, otherwise false
            conditions.forEach() {
                // first false will return false immediately
                if(!it.invoke(t)) return false
            }
            return true
        } else {
            // first predicate is true then return true, otherwise false
            conditions.forEach() {
                // the first true will stop the loop and return true
                if(it.invoke(t)) return true
            }
            return false
        }
    }
}
