package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvConverter {
    public static void main(String[] args) {
        String inputFile = "src/main/resources/statements.csv";
        String outputFile = "src/main/resources/Taxer.csv";

        File input = new File(inputFile);
        if (!input.exists() || input.isDirectory()) {
            System.err.println("Input file does not exist or is a directory.");
            return;
        }

        List<CsvRow> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "windows-1251"))) {
            // Пропустити перший рядок
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.replace("\"", "").split(";");

                // Перевірка, чи достатньо елементів у масиві
                if (columns.length < 17) {
                    System.err.println("Skipping line with insufficient columns: " + line);
                    continue;
                }

                if (columns[15].contains("Гр.экв.продажу")) {
                    continue;
                }

                CsvRow row;
                if (columns[15].contains("Перерахування коштів для продажу валюти")) {
                    row = new CsvRow(
                            columns[0], // taxId
                            columns[4], // dateTime
                            columns[13].isEmpty() ? columns[14] : columns[13], // amount
                            columns[15], // comment
                            "Обмін валюти", // currencyExchange
                            "Основний дохід", // mainIncome
                            columns[3].equals("EUR") ? "«ІТ Підприємець» EUR" : "«ІТ Підприємець» UAH", // sourceAccountName
                            columns[3], // sourceAccountCurrency
                            "«ІТ Підприємець» UAH", // destinationAccountName
                            "UAH" // destinationAccountCurrency
                    );
                } else {
                    row = new CsvRow(
                            columns[0], // taxId
                            columns[4], // dateTime
                            columns[13].isEmpty() ? columns[14] : columns[13], // amount
                            columns[15], // comment
                            "", // currencyExchange
                            columns[13].isEmpty() ? "Основний дохід" : "Комісія банку", // mainIncome
                            columns[3].equals("EUR") ? "«ІТ Підприємець» EUR" : "«ІТ Підприємець» UAH", // sourceAccountName
                            columns[3], // sourceAccountCurrency
                            "", // destinationAccountName
                            "" // destinationAccountCurrency
                    );
                }
                rows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Запис у файл з кінця
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
            for (int i = rows.size() - 1; i >= 0; i--) {
                writer.write(rows.get(i).toString() + "\n");
            }
            System.out.println("Conversion completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
