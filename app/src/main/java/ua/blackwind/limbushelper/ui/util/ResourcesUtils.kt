package ua.blackwind.limbushelper.ui.util

import androidx.compose.ui.graphics.Color
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.common.DamageType
import ua.blackwind.limbushelper.domain.common.Effect
import ua.blackwind.limbushelper.domain.common.Sin
import ua.blackwind.limbushelper.ui.theme.*


fun getSinColor(sin: Sin): Color {
    return when (sin) {
        Sin.WRATH -> wrath
        Sin.LUST -> lust
        Sin.SLOTH -> sloth
        Sin.GLUTTONY -> gluttony
        Sin.GLOOM -> gloom
        Sin.PRIDE -> pride
        Sin.ENVY -> envy
    }
}

fun getDamageTypeIcon(damageType: DamageType): Int {
    return when (damageType) {
        DamageType.SLASH -> R.drawable.slash_hex_ic
        DamageType.PIERCE -> R.drawable.pierce_hex_ic
        DamageType.BLUNT -> R.drawable.blunt_hex_ic
    }
}

fun getSinnerIconById(id: Int): Int {
    return when (id) {
        1 -> R.drawable.yi_sang_50_ic
        2 -> R.drawable.faust_50_ic
        3 -> R.drawable.don_50_ic
        4 -> R.drawable.ryoshu_50_ic
        5 -> R.drawable.mersault_50_ic
        6 -> R.drawable.hong_lu_50_ic
        7 -> R.drawable.heathcliff_50_ic
        8 -> R.drawable.ishmael_50_ic
        9 -> R.drawable.rodion_50_ic
        10 -> R.drawable.sinclair_50_ic
        11 -> R.drawable.outis_50_ic
        12 -> R.drawable.gregor_50_ic
        else -> throw java.lang.IllegalArgumentException("Sinner id:$id out of range 1-12")
    }
}

fun getSinIcon(sin: Sin): Int {
    return when (sin) {
        Sin.WRATH -> R.drawable.wrath_50_ic
        Sin.LUST -> R.drawable.lust_50_ic
        Sin.SLOTH -> R.drawable.sloth_50_ic
        Sin.GLUTTONY -> R.drawable.gluttony_50_ic
        Sin.GLOOM -> R.drawable.gloom_50_ic
        Sin.PRIDE -> R.drawable.pride_50_ic
        Sin.ENVY -> R.drawable.envy_50_ic
    }
}

fun getEffectIcon(effect: Effect): Int {
    return when (effect) {
        Effect.BLEED -> R.drawable.bleed_ic
        Effect.PARALYSIS -> R.drawable.paralisys_ic
        Effect.RUPTURE -> R.drawable.rupture_ic
        Effect.SINKING -> R.drawable.sink_ic
        Effect.TREMOR -> R.drawable.tremor_ic
        Effect.BURN -> R.drawable.burn_ic
        Effect.BIND -> R.drawable.bind_ic
        Effect.FRAGILE -> R.drawable.fragile_ic
        Effect.ATTACK_UP -> R.drawable.att_up_ic
        Effect.ATTACK_DOWN -> R.drawable.att_down_ic
        Effect.POISE -> R.drawable.poise_ic
        Effect.PROTECT -> R.drawable.protect_ic
        Effect.HASTE -> R.drawable.haste_ic
        Effect.CHARGE -> R.drawable.charge_ic
        Effect.AMMO -> R.drawable.ammo_ic
        Effect.DEFENSE_UP -> R.drawable.def_up_ic
        Effect.DEFENSE_DOWN -> R.drawable.def_down_ic
        Effect.HEAL -> R.drawable.heal_ic
    }
}