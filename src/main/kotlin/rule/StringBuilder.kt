package rule

/*
fun buildString(builderAction: StringBuilder.() -> Unit ): String {
    val sb = StringBuilder()
    sb.builderAction()
    return sb.toString()
}
*/

fun buildString(builderAction: StringBuilder.() -> Unit ): String  =
    StringBuilder().apply(builderAction).toString()

val s = buildString {
    append("Hello, ")
    append("World!")
}

fun main(args: Array<String>) {
    println(s)
}
