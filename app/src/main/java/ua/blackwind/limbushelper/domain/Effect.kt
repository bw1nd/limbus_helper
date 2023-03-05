package ua.blackwind.limbushelper.domain

enum class Effect {
    BLEED,
    PARALYSIS,
    RUPTURE,
    SINKING,
    TREMOR,
    BURN,
    BIND,
    FRAGILE,
    CURSE,
    OFFENCE_UP,
    OFFENCE_DOWN,
    DEFENSE_UP,
    DEFENSE_DOWN,
    ATTACK_UP,
    ATTACK_DOWN,
    POISE,
    PROTECT,
    HASTE,
    CHARGE,
    AMMO,
    HEAL
}

fun String.parseStringToEffectsList(): Effect {
    return Effect.values().toList().find { it.name == this }
        ?: throw Exception("Can't parse string")
}

