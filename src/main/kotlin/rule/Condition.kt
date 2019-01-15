package rule

typealias Predicate<T> = (T) -> Boolean

open class Condition<T> : Predicate<T> {

    val condition: Predicate<T>

    constructor(condition: Predicate<T>) {
        this.condition = condition
    }

    override fun invoke(t: T): Boolean {
        return condition.invoke(t)
    }
}


// Not
operator fun <T> Predicate<T>.not() = { e : T -> !this(e) }

// Or
infix fun <T> Predicate<T>.or(other: Predicate<T>) : CompondCondition<T> = CompondCondition(CompondOperator.OR, listOf(Condition(this), Condition(other)))
inline fun <reified T> or(vararg predicates: Predicate<T>): CompondCondition<T>  = CompondCondition(CompondOperator.OR, predicates.toList().map{Condition(it)}.toList())

infix fun <T> Condition<T>.or(other: Condition<T>) : CompondCondition<T> = CompondCondition(CompondOperator.OR, listOf(this, other))
inline fun <reified T> or(vararg conditions: Condition<T>): CompondCondition<T>  = CompondCondition(CompondOperator.OR, conditions.toList())

// And
infix fun <T> Predicate<T>.and(other: Predicate<T>) : CompondCondition<T> = CompondCondition(CompondOperator.AND, listOf(Condition(this), Condition(other)))
inline fun <reified T> and(vararg predicates: Predicate<T>): CompondCondition<T>  = CompondCondition(CompondOperator.AND, predicates.toList().map{Condition(it)}.toList())

infix fun <T> Condition<T>.and(other: Condition<T>): CompondCondition<T> = CompondCondition(CompondOperator.AND, listOf(this, other))
inline fun <reified T> and(vararg conditions: Condition<T>): CompondCondition<T> = CompondCondition(CompondOperator.AND, conditions.toList())
