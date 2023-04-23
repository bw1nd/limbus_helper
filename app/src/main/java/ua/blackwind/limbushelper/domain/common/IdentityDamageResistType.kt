package ua.blackwind.limbushelper.domain.common

enum class IdentityDamageResistType(val scale: Float) {
    NORMAL(1f),
    INEFF(.5f),
    FATAL(2f)
}