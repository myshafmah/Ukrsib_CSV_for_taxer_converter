package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.dto.UkrsibOnline;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public static List<UkrsibOnline> readExcel(String filePath) {
        List<UkrsibOnline> operations = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Отримуємо перший лист
            int rowCount = sheet.getPhysicalNumberOfRows();

            // Пропускаємо перший рядок, якщо це заголовки
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);

                String dateTime = row.getCell(0).getStringCellValue();   // Дата і час операції
                String details = row.getCell(1).getStringCellValue();    // Деталі операції
                int mcc = (int) row.getCell(2).getNumericCellValue();    // MCC
                double amountInCardCurrency = row.getCell(3).getNumericCellValue();  // Сума в валюті картки
                double amountInOperationCurrency = row.getCell(4).getNumericCellValue(); // Сума в валюті операції
                String currency = row.getCell(5).getStringCellValue();   // Валюта
                Double exchangeRate = row.getCell(6) != null ? row.getCell(6).getNumericCellValue() : null; // Курс
                Double commissionAmount = row.getCell(7) != null ? row.getCell(7).getNumericCellValue() : null; // Комісія
                Double cashbackAmount = row.getCell(8) != null ? row.getCell(8).getNumericCellValue() : null; // Кешбек
                double balanceAfterOperation = row.getCell(9).getNumericCellValue(); // Залишок після операції

                // Створюємо DTO і додаємо до списку
                UkrsibOnline operation = new UkrsibOnline(dateTime, details, mcc, amountInCardCurrency,
                        amountInOperationCurrency, currency, exchangeRate, commissionAmount, cashbackAmount, balanceAfterOperation);
                operations.add(operation);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return operations;
    }
}

