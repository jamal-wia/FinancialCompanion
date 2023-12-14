package com.financialcompanion.android.core.domain.extension

infix fun <T1, T2, T3> Pair<T1, T2>.trip(third: T3): Triple<T1, T2, T3> {
    return Triple(this.first, this.second, third)
}
