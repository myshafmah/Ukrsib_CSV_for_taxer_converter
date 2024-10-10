package org.example;

import org.example.dto.Cashew;
import org.example.dto.MonoCSV;

public class CashewToMonoCSVConverter {

    // Метод для конвертації Cashew в MonoCSV
    public static MonoCSV cashewToMonoCSV(Cashew cashew) {
        MonoCSV monoCSV = new MonoCSV();
        monoCSV.setDateTime(cashew.getDate());  // Встановлюємо дату і час операції
        monoCSV.setDetails(cashew.getTitle());  // Використовуємо title як деталі
        monoCSV.setAmountInCardCurrency(cashew.getAmount()); // Встановлюємо суму
        monoCSV.setAmountInOperationCurrency(cashew.getAmount()); // Валюта операції = валюті картки
        monoCSV.setCurrency(cashew.getCurrency());  // Встановлюємо валюту
        monoCSV.setBalanceAfterOperation(0.0); // Потрібно визначити, або залишити за замовчуванням
        monoCSV.setCommissionAmount(null);  // Поки що комісія не використовується
        monoCSV.setCashbackAmount(null);  // Поки що кешбек не використовується
        monoCSV.setMcc(0);  // MCC не вказано в Cashew, можливо, необхідно отримати ззовні
        return monoCSV;
    }

    // Метод для конвертації MonoCSV в Cashew
    public static Cashew monoCSVToCashew(MonoCSV monoCSV) {
        Cashew cashew = new Cashew();
        cashew.setDate(monoCSV.getDateTime()); // Встановлюємо дату з MonoCSV
        cashew.setTitle(monoCSV.getDetails()); // Використовуємо details як title
        cashew.setAmount(monoCSV.getAmountInCardCurrency()); // Використовуємо суму в валюті картки
        cashew.setCurrency(monoCSV.getCurrency()); // Встановлюємо валюту
        cashew.setIncome(monoCSV.getAmountInCardCurrency() > 0); // Якщо сума позитивна, це дохід
        cashew.setCategoryName("Unknown"); // Категорію потрібно визначити додатково
        cashew.setSubcategoryName("Unknown"); // Підкатегорію потрібно визначити додатково
        cashew.setColor("Unknown"); // Колір можна визначити додатково
        return cashew;
    }
}

