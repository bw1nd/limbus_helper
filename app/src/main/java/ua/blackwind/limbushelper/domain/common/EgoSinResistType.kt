package ua.blackwind.limbushelper.domain.common

enum class EgoSinResistType(damageScale: Float) {
    NORMAL(1f),
    INEFF(.5f),
    ENDURE(.75f),
    FATAL(2f)
}
