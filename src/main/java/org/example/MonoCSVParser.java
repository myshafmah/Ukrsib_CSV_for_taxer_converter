package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.dto.MonoCSV;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MonoCSVParser {


    public static void main(String[] args) {
        String file = "data.csv";  // Шлях до вашого CSV-файлу

        List<MonoCSV> transactions = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> columns = reader.readAll();

            for (String[] column : columns) {
                // Пропускаємо заголовки (перший рядок)
                if (column[0].equals("Дата i час операції")) {
                    continue;
                }

                // Створюємо DTO для кожного рядка CSV
                MonoCSV transaction = new MonoCSV(
                        column[0],                               // Дата і час операції
                        column[1],                               // Деталі операції
                        Integer.parseInt(column[2]),             // MCC
                        Double.parseDouble(column[3]),           // Сума в валюті картки (UAH)
                        Double.parseDouble(column[4]),           // Сума в валюті операції
                        column[5],                               // Валюта
                        column[6].equals("—") ? null : Double.parseDouble(column[6]), // Курс
                        column[7].equals("—") ? null : Double.parseDouble(column[7]), // Сума комісій (UAH)
                        column[8].equals("—") ? null : Double.parseDouble(column[8]), // Сума кешбеку (UAH)
                        Double.parseDouble(column[9])            // Залишок після операції
                );

                // Додаємо до списку транзакцій
                transactions.add(transaction);
            }

            // Виведення зчитаних транзакцій
            for (MonoCSV transaction : transactions) {
                System.out.println(transaction);
            }

        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }


}
