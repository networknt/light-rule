package rule

typealias Predicate<T> = (T) -> Boolean

// Not
operator fun <T> Predicate<T>.not() = { e : T -> !this(e) }

// Or
infix fun <T> Predicate<T>.or(other: Predicate<T>) : Specification<T> = CompondSpecification(CompondOperator.OR, listOf(this, other))
inline fun <reified T> or(vararg predicates: Predicate<T>): Specification<T>  = CompondSpecification(CompondOperator.OR, predicates.toList())

//inline fun <reified T> or(predicates: Iterable<Predicate<T>?>): Predicate<T> {
//    return combineConditions(predicates, Predicate<T>::or)
//}
//
//// Combines Conditions with an operation
//inline fun <reified T> combineConditions(conditions: Iterable<Predicate<T>?>, operation: Predicate<T>.(Predicate<T>) -> Predicate<T>): Predicate<T> {
//    return conditions.filterNotNull().fold(emptySpecification()) { existing, new -> existing.operation(new) }
//}

infix fun <T> Predicate<T>.and(other: Predicate<T>): Specification<T> = CompondSpecification(CompondOperator.AND, listOf(this, other))
inline fun <reified T> and(vararg predicates: Predicate<T>): Specification<T> = CompondSpecification(CompondOperator.AND, predicates.toList())
