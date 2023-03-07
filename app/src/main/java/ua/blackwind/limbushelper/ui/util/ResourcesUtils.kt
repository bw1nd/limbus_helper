package ua.blackwind.limbushelper.ui.util

import androidx.compose.ui.graphics.Color
import ua.blackwind.limbushelper.R
import ua.blackwind.limbushelper.domain.DamageType
import ua.blackwind.limbushelper.domain.Effect
import ua.blackwind.limbushelper.domain.Sin
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
        DamageType.SLASH -> R.drawable.slash_border_ic
        DamageType.PIERCE -> R.drawable.pierce_border_ic
        DamageType.BLUNT -> R.drawable.blunt_border_ic
    }
}

fun getSinIcon(sin: Sin): Int {
    return when (sin) {
        Sin.WRATH -> R.drawable.wrath_ic
        Sin.LUST -> R.drawable.lust_ic
        Sin.SLOTH -> R.drawable.sloth_ic
        Sin.GLUTTONY -> R.drawable.gluttony_ic
        Sin.GLOOM -> R.drawable.gloom_ic
        Sin.PRIDE -> R.drawable.pride_ic
        Sin.ENVY -> R.drawable.envy_ic
    }
}

fun getEffectIcon(effect: Effect): Int {
    //TODO add other icons
    return when (effect) {
        Effect.BLEED -> R.drawable.bleed_ic
        Effect.PARALYSIS -> R.drawable.bleed_ic
        Effect.RUPTURE -> R.drawable.rupture_ic
        Effect.SINKING -> R.drawable.sink_ic
        Effect.TREMOR -> R.drawable.tremor_ic
        Effect.BURN -> R.drawable.burn_ic
        Effect.BIND -> R.drawable.rupture_ic
        Effect.FRAGILE -> R.drawable.rupture_ic
        Effect.CURSE -> R.drawable.rupture_ic
        Effect.ATTACK_UP -> R.drawable.dmg_up_ic
        Effect.ATTACK_DOWN -> R.drawable.dmg_up_ic
        Effect.OFFENCE_UP -> R.drawable.power_up_ic
        Effect.OFFENCE_DOWN -> R.drawable.power_up_ic
        Effect.POISE -> R.drawable.poise_ic
        Effect.PROTECT -> R.drawable.poise_ic
        Effect.HASTE -> R.drawable.poise_ic
        Effect.CHARGE -> R.drawable.charge_ic
        Effect.AMMO -> R.drawable.burn_ic
        Effect.DEFENSE_UP -> R.drawable.dmg_up_ic
        Effect.DEFENSE_DOWN -> R.drawable.dmg_up_ic
        Effect.HEAL -> R.drawable.blunt_ic
    }
}