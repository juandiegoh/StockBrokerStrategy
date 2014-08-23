package com.jherenu.stocks.common

class DateFromStringCreator {

    def static createFromFormat(dateString, format = "yyyy/MM/dd") {
        return Date.parse(format, dateString)
    }
}
