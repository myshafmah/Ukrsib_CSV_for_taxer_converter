package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashewToMonoDTO {
    private String account;
    private String title;
    private String note;
    private String dateTime;          // Дата і час операції
    private String details;           // Деталі операції
    private int mcc;                  // MCC (Merchant Category Code)
    private double amountInCardCurrency;  // Сума в валюті картки (UAH)
    private double amountInOperationCurrency; // Сума в валюті операції
    private String currency;          // Валюта операції
    private Double exchangeRate;      // Курс
    private Double commissionAmount;  // Сума комісій (UAH)
    private Double cashbackAmount;    // Сума кешбеку (UAH)
    private double balanceAfterOperation; // Залишок після операції
    private boolean income;
    private String type;
    private String categoryName;
    private String subcategoryName;
    private String color;
    private String icon;
    private String emoji;
    private String budget;
    private String objective;
}

