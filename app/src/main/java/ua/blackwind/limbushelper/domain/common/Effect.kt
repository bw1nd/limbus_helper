package ua.blackwind.limbushelper.domain.common

enum class Effect {
    BLEED,
    BURN,
    SINKING,
    RUPTURE,
    POISE,
    PARALYSIS,
    TREMOR,
    PROTECT,
    FRAGILE,
    ATTACK_UP,
    ATTACK_DOWN,
    CHARGE,
    DEFENSE_UP,
    DEFENSE_DOWN,
    HEAL,
    HASTE,
    BIND,
    AMMO,
}

fun String.parseStringToEffectsList(): Effect {
    return Effect.values().toList().find { it.name == this }
        ?: throw Exception("Can't parse string")
}

