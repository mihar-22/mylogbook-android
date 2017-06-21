package com.mylb.mylogbook.data.util

import com.mylb.mylogbook.data.network.Network
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter

fun String.toDateTime(formatter: DateTimeFormatter = Network.dateTimeFormat) = DateTime.parse(this, formatter)

