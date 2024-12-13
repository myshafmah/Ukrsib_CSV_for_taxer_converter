package converter;

import com.opencsv.exceptions.CsvException;
import converter.dto.UkrsibOnline;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExcelReader {

    public List<UkrsibOnline> dynamicExcelFileReader(String name, String cardColor) {
        String userHome = System.getProperty("user.home"); // Отримуємо шлях до поточного користувача
        String downloadsFolder = userHome + "\\Downloads\\report\\ukrsib" + "\\" + name + "\\" + "Debet" + "\\"; // Шлях до папки Downloads

        // Створюємо шаблон для пошуку файлу
        String filePrefix = "Список операцій по рахунку 26208810325732";
        String fileExtension = ".xlsx";

        // Шукаємо файл за шаблоном у теці Downloads
        Optional<File> file = ConverterApplication.findLatestFile(downloadsFolder, filePrefix, fileExtension);

        // Якщо файл знайдений, читаємо його вміст
        if (file.isPresent()) {
            System.out.println("Файл знайдено: " + file.get().getName());
            try {
                // Парсимо файл CSV і повертаємо список MonoCSV
                return parseExcelFile(file.get());
            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("\n" + "Файл не знайдено. " + downloadsFolder + filePrefix + fileExtension);
        }
        return new ArrayList<>();
    }

    public static List<UkrsibOnline> parseExcelFile(File excelFile) throws IOException, CsvException {
        List<UkrsibOnline> operations = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Отримуємо перший лист
            int rowCount = sheet.getPhysicalNumberOfRows();

            // Пропускаємо перший рядок, якщо це заголовки
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);

                String status = row.getCell(0).getStringCellValue();  // Статус операції
                String dateTime = row.getCell(1).getStringCellValue();  // Дата операції
                String details = row.getCell(2).getStringCellValue();   // Деталі операції
                String account = row.getCell(3).getStringCellValue();   // Рахунок/картка
                String category = row.getCell(4).getStringCellValue();   // Категорія операції
                double amount = row.getCell(5).getNumericCellValue();  // Сума операції
                String currency = row.getCell(6).getStringCellValue();   // Валюта операції

                // Створюємо DTO і додаємо до списку
                UkrsibOnline operation = new UkrsibOnline(
                        status,
                        dateTime,
                        details,
                        account,
                        category,
                        amount,
                        currency
                );
                operations.add(operation);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return operations;
    }
}

