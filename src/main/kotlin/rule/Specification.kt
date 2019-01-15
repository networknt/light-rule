package rule

interface Specification<T> {
    /**
     * The evaluate method will take an object of T and return a boolean
     */
    fun evaluate(t: T) : Boolean
}
