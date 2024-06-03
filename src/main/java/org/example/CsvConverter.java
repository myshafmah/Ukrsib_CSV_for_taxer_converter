package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CsvConverter {
    public static void main(String[] args) {
        String inputFile = "src/main/resources/statements.csv";
        String outputFile = "src/main/resources/Taxer.csv";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "windows-1251"));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {

            // Skip the first row
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.replace("\"", "").split(";");

                // Check if the array has enough elements
                if (columns.length >= 17) {
                    if (!columns[15].contains("Гр.экв.продажу")) {
                        if (columns[15].contains("Перерахування коштів для продажу валюти")) {
                            String outputLine = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                                    columns[0],
                                    columns[4],
                                    columns[13].isEmpty() ? columns[14] : columns[13],
                                    columns[15],
                                    "Обмін валюти",
                                    "Основний дохід",
                                    columns[3].equals("EUR") ? "«ІТ Підприємець» EUR" : "«ІТ Підприємець» UAH",
                                    columns[3],
                                    "«ІТ Підприємець» UAH",
                                    "UAH"
                            );
                            writer.write(outputLine + "\n");
                        } else {

                            // Build the output line in the desired format
                            String outputLine = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                                    columns[0],
                                    columns[4],
                                    columns[13].isEmpty() ? columns[14] : columns[13],
                                    columns[15],
                                    columns[13].isEmpty() ? "Дохід" : "Витрата",
                                    columns[13].isEmpty() ? "Основний дохід" : "Комісія банку",
                                    columns[3].equals("EUR") ? "«ІТ Підприємець» EUR" : "«ІТ Підприємець» UAH",
                                    columns[3]);
                            writer.write(outputLine + "\n");
                        }
                    }
                } else {
                    System.err.println("Skipping line with insufficient columns: " + line);
                }
            }
            System.out.println("Conversion completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}