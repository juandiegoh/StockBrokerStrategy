package com.jherenu.stocks.factory

import com.jherenu.stocks.domain.Movement

class MovementFactory {

    Movement create(date, companyName, operation, amount) {
        return new Movement(
                date: date,
                companyName: companyName,
                operation: operation,
                amount: amount
        )
    }
}
