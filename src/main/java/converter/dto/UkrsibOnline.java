package converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UkrsibOnline {

    private String status;          // Статус операції
    private String dateTime;          // Дата операції
    private String details;           // Деталі операції
    private String account;           // Рахунок/картка
    private String category;           // Категорія операції
    private double amount;           // Сума операції
    private String currency;           // Валюта операції
}
