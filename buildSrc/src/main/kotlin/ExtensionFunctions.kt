fun <T> List<T>.ifNotEmpty(block: () -> Unit): List<T> {
    if (this.isNotEmpty()) {
        block()
    }
    return this
}

fun Boolean.ifTrue(block: () -> Unit) {
    if (this) {
        block()
    }
}