package org.mytonwallet.app_air.walletcontext.utils

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import kotlin.math.abs

fun Double.toString(
    decimals: Int,
    currency: String,
    currencyDecimals: Int,
    smartDecimals: Boolean,
    showPositiveSign: Boolean = false
): String? {
    val bigInteger = toBigInteger(decimals)
    return bigInteger?.toString(
        decimals,
        currency,
        if (smartDecimals) bigInteger.smartDecimalsCount(currencyDecimals) else currencyDecimals,
        showPositiveSign
    )
}

fun Double.toBigInteger(digits: Int): BigInteger? {
    if (!this.isFinite())
        return null
    val scaledValue = BigDecimal(this).setScale(digits, RoundingMode.FLOOR)
    val formattedString = scaledValue.stripTrailingZeros().toPlainString()
    val (integralPart, fractionalPart) = if (formattedString.contains(".")) {
        val (integral, fractional) = formattedString.split(".", limit = 2)
        Pair(integral, fractional)
    } else {
        Pair(formattedString, "")
    }

    val fractionalPartPadded = fractionalPart.take(digits).padEnd(digits, '0')
    val finalString = integralPart + fractionalPartPadded

    return finalString.toBigInteger()
}

fun Double.smartDecimalsCount(tokenDecimals: Int): Int {
    if (tokenDecimals <= 2) {
        return tokenDecimals
    }
    val amount = abs(this)
    if (amount == 0.0) {
        return 0
    }
    if (amount >= 1) {
        return maxOf(2, 3 - amount.toInt().toString().length)
    }
    var newAmount = abs(amount)
    var multiplier = 0
    while (newAmount < 2) {
        newAmount *= 10
        multiplier += 1
    }
    return minOf(tokenDecimals, multiplier)
}
