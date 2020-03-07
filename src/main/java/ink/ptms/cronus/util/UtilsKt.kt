package ink.ptms.cronus.util

object UtilsKt {

    fun <T> all(list: List<T>, a: (T) -> (Boolean)): Boolean {
        return list.all { a.invoke(it) }
    }

    fun <T> any(list: List<T>, a: (T) -> (Boolean)): Boolean {
        return list.any { a.invoke(it) }
    }

    fun <T> none(list: List<T>, a: (T) -> (Boolean)): Boolean {
        return list.none { a.invoke(it) }
    }
}