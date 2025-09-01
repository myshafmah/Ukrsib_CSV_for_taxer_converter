package converter;

import com.opencsv.exceptions.CsvException;
import converter.dto.UkrsibOnline;
import converter.util.FileUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ExcelReader {

    private static final Logger logger = Logger.getLogger(ExcelReader.class.getName());

    public List<UkrsibOnline> dynamicExcelFileReader(String name, String cardColor) {
        String userHome = System.getProperty("user.home"); // Отримуємо шлях до поточного користувача
        String downloadsFolder = Paths.get(userHome, "Downloads", "report", "ukrsib", name, "Debet").toString(); // Кросплатформений шлях

        // Створюємо шаблон для пошуку файлу
        String filePrefix = "Список операцій";
        String fileExtension = ".xlsx";

        // Шукаємо файл за шаблоном у теці Downloads
        Optional<File> file = FileUtils.findLatestFile(downloadsFolder, filePrefix, fileExtension);

        // Якщо файл знайдений, читаємо його вміст
        if (file.isPresent()) {
            logger.info("Файл знайдено: " + file.get().getName());
            try {
                // Парсимо файл Excel і повертаємо список операцій
                return parseExcelFile(file.get());
            } catch (IOException | CsvException e) {
                logger.severe("Помилка читання Excel: " + e.getMessage());
            }
        } else {
            logger.fine("Файл не знайдено: " + downloadsFolder + File.separator + filePrefix + fileExtension);
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
            logger.severe("Помилка читання Excel: " + e.getMessage());
        }

        return operations;
    }
}

