package com.hardzei.coronavirusapp.view

import java.text.DecimalFormat

fun Int.formatToStringWithDiv(): String {
    return DecimalFormat("###,###").format(this)
}
