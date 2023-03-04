package com.example.limbushelper.domain

enum class Effect {
    BLEED,
    PARALYZE,
    RUPTURE,
    SINKING,
    TREMOR,
    BURN,
    BIND,
    FRAGILITY,
    CURSE,
    DAMAGE_UP,
    DAMAGE_DOWN,
    POWER_UP,
    POWER_DOWN,
    POISE,
    PROTECT,
    HASTE,
    CHARGE,
    AMMO
}

fun String.parseStringToEffectsList(): Effect {
    return Effect.values().toList().find { it.name == this }
        ?: throw Exception("Can't parse string")
}

