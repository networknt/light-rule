package rule

class Specifications<T> : Specification<T> {

    val spec: Specification<T>

    constructor(spec: Specification<T>) {
        this.spec = spec
    }

    override fun evaluate(t: T): Boolean {
        return spec.evaluate(t)
    }
}

