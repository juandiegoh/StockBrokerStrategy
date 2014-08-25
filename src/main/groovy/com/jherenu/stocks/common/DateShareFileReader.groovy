package com.jherenu.stocks.common

import com.jherenu.stocks.domain.DateShare

class DateShareFileReader {

    static def parseFileFromResource(fileName) {
        this.parseFile() { Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName) }
    }

    static def parseFromFile(fileName) {
        this.parseFile() { new FileInputStream(fileName) }
    }

    private static def parseFile(inputStreamCreatorClosure) {
        InputStream inputFile = inputStreamCreatorClosure()
        String[] lines = inputFile.text.split('\n')
        List<String[]> rows = lines.collect {it.split(';')}
        return rows.tail().collect { String[] it ->
            def(companyName, date, price) = it
            new DateShare(companyName: companyName.trim(), date: getDateFromString(date.trim()), price: getPriceFromString(price.trim()))
        }
    }


    static BigDecimal getPriceFromString(String priceString) {
        def priceToNum = priceString.replace('$', '').replace(",", ".")
        return new BigDecimal(priceToNum)
    }

    static Date getDateFromString(String stringDate) {
        def (day, month, year) = stringDate.split('/')
        return DateFromStringCreator.createFromFormat("${year}/${month}/${day}")
    }
}
