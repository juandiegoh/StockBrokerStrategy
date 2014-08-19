package com.jherenu.stocks

public interface SimulatorStrategy {

    def decideFromPreviousSharesAndActualShare(previousDateShares, actualDateShare)
}