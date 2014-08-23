package com.jherenu.stocks.domain

class Movement {
    Date date
    MovementOperation operation
    def companyName
    def amount

    String toString() {
        return "MOVEMENT -> ${date} | ${operation} | ${companyName} | ${amount}"
    }
}
