package com.jherenu.stocks

class DateSharesFixture {

    static String TEST_FILE_NAME = "big_example.csv"

    static def createBigExample() {
        //InputStream inputFile = DateSharesFixture.class.getClass().getClassLoader().getResourceAsStream(TEST_FILE_NAME)
        InputStream inputFile = Thread.currentThread().getContextClassLoader().getResourceAsStream(TEST_FILE_NAME)
        String[] lines = inputFile.text.split('\n')
        List<String[]> rows = lines.collect {it.split(',')}
        return rows.tail().collect { String[] it ->
            def(companyName, date, price) = it
            new DateShare(companyName: companyName.trim(), date: getDateFromString(date.trim()), price: new BigDecimal(getPriceFromString(price.trim())))
        }
    }

    static BigDecimal getPriceFromString(String priceString) {
        return new BigDecimal(priceString.replace('$', '').replace(",", "."))
    }

    static Date getDateFromString(String stringDate) {
        def (day, month, year) = stringDate.split('/')
        return new Date(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day))
    }
}
