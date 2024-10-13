package converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonoCSV {
    private String dateTime;          // Дата і час операції
    private String details;           // Деталі операції
    private Integer mcc;                  // MCC (Merchant Category Code)
    private Double amountInCardCurrency;  // Сума в валюті картки (UAH)
    private Double amountInOperationCurrency; // Сума в валюті операції
    private String currency;          // Валюта операції
    private Double exchangeRate;      // Курс
    private Double commissionAmount;  // Сума комісій (UAH)
    private Double cashbackAmount;    // Сума кешбеку (UAH)
    private Double balanceAfterOperation; // Залишок після операції
}
