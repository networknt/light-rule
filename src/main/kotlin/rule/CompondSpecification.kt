package rule

class CompondSpecification<T> : Specification<T> {
    val operator: CompondOperator
    val predicates: List<Predicate<T>>

    constructor(operator: CompondOperator, predicates: List<Predicate<T>>) {
        this.operator = operator
        this.predicates = predicates
    }

    override fun evaluate(t: T): Boolean {
        if(operator == CompondOperator.AND) {
            // all predicates must be true to return true, otherwise false
            predicates.forEach() {
                // first false will return false immediately
                if(!it(t)) return false
            }
            return true
        } else {
            // first predicate is true then return true, otherwise false
            predicates.forEach() {
                // the first true will stop the loop and return true
                if(it(t)) return true
            }
            return false
        }
    }
}