package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.dto.Cashew;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CashewParser {

        public static void main(String[] args) {
            String file = "data.csv";  // Шлях до вашого CSV-файлу

            List<Cashew> transactions = new ArrayList<>();

            try (CSVReader reader = new CSVReader(new FileReader(file))) {
                List<String[]> columns = reader.readAll();

                stringIteration(columns, transactions);

                // Виведення зчитаних транзакцій
                for (Cashew transaction : transactions) {
                    System.out.println(transaction);
                }

            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }
        }

    private static void stringIteration(List<String[]> columns, List<Cashew> transactions) {
        for (String[] column : columns) {
            // Пропускаємо заголовки (перший рядок)
            if (column[0].equals("account")) {
                continue;
            }

            // Створюємо DTO для кожного рядка CSV
            Cashew transaction = new Cashew(
                    column[0],                                  // account
                    Double.parseDouble(column[1]),              // amount
                    column[2],                                  // currency
                    column[3],                                  // title
                    column[4],                                  // note
                    column[5],                                  // date
                    Boolean.parseBoolean(column[6]),            // income
                    column[7],                                  // type
                    column[8],                                  // category name
                    column[9],                                  // subcategory name
                    column[10],                                 // color
                    column[11],                                 // icon
                    column[12],                                 // emoji
                    column[13],                                 // budget
                    column[14]                                  // objective
            );

            // Додаємо до списку транзакцій
            transactions.add(transaction);
        }
    }


}
