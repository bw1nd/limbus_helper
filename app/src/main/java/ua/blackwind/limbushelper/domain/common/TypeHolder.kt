package ua.blackwind.limbushelper.domain.common

/**
 * Type holder representing filter state which can have type or be empty.
 */
sealed class TypeHolder<out T: Any> {
    object Empty: TypeHolder<Nothing>()
    data class Value<T: Any>(val value: T): TypeHolder<T>()
}