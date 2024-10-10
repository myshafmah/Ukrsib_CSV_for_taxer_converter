package org.example;

public class CsvRow {
    private String taxId; // ІПН/ЄДРПОУ
    private String dateTime; // дата, час
    private String amount; // сума
    private String comment; // коментар
    private String currencyExchange; // обмін валюти
    private String mainIncome; // основний дохід
    private String sourceAccountName; // назва рахунку з якого виконується обмін
    private String sourceAccountCurrency; // валюта рахунку з якого виконується обмін
    private String destinationAccountName; // назва рахунку на який будуть переказувати кошти після обміну
    private String destinationAccountCurrency; // валюта рахунку на який будуть переказувати кошти після обміну

    // Конструктор
    public CsvRow(String taxId, String dateTime, String amount, String comment, String currencyExchange,
                  String mainIncome, String sourceAccountName, String sourceAccountCurrency,
                  String destinationAccountName, String destinationAccountCurrency) {
        this.taxId = taxId;
        this.dateTime = dateTime;
        this.amount = amount;
        this.comment = comment;
        this.currencyExchange = currencyExchange;
        this.mainIncome = mainIncome;
        this.sourceAccountName = sourceAccountName;
        this.sourceAccountCurrency = sourceAccountCurrency;
        this.destinationAccountName = destinationAccountName;
        this.destinationAccountCurrency = destinationAccountCurrency;
    }

    // Гетер для створення рядка CSV
    @Override
    public String toString() {
        return String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                taxId, dateTime, amount, comment, currencyExchange, mainIncome,
                sourceAccountName, sourceAccountCurrency, destinationAccountName, destinationAccountCurrency);
    }
}