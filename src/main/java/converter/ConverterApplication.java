package converter;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import converter.dto.MonoCSV;
import converter.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;


public class ConverterApplication {
    private static final Logger logger = Logger.getLogger(ConverterApplication.class.getName());
    private static final String HEADER_DATE_TIME = "Дата i час операції";

    public List<MonoCSV> dynamicCSVFileReader(String name, String cardColor) {
        String userHome = System.getProperty("user.home"); // Отримуємо шлях до поточного користувача
        String downloadsFolder = Paths.get(userHome, "Downloads", "report", "mono", name, cardColor).toString(); // Кросплатформений шлях

        // Створюємо шаблон для пошуку файлу
        String filePrefix = "report_";
        String fileExtension = ".csv";

        // Шукаємо файл за шаблоном у теці Downloads
        Optional<File> file = FileUtils.findLatestFile(downloadsFolder, filePrefix, fileExtension);

        // Якщо файл знайдений, читаємо його вміст
        if (file.isPresent()) {
            logger.info("Файл знайдено: " + file.get().getName());
            try {
                // Парсимо файл CSV і повертаємо список MonoCSV
                return parseCSVFile(file.get());
            } catch (IOException | CsvException e) {
                logger.severe("Помилка читання CSV: " + e.getMessage());
            }
        } else {
            logger.fine("Файл не знайдено: " + downloadsFolder + File.separator + filePrefix + fileExtension);
        }
        return new ArrayList<>();
    }

    // Метод для парсингу CSV-файлу в об'єкти MonoCSV
    private List<MonoCSV> parseCSVFile(File csvFile) throws IOException, CsvException {
        List<MonoCSV> transactions = new ArrayList<>();

        try (CSVReader reader = new CSVReader(Files.newBufferedReader(csvFile.toPath(), StandardCharsets.UTF_8))) {
            List<String[]> columns = reader.readAll();

            for (String[] column : columns) {
                // Пропускаємо заголовки (перший рядок)
                if (HEADER_DATE_TIME.equals(column[0])) {
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
                        parseNullableDouble(column[6]),          // Курс
                        parseNullableDouble(column[7]),          // Сума комісій (UAH)
                        parseNullableDouble(column[8]),          // Сума кешбеку (UAH)
                        Double.parseDouble(column[9])            // Залишок після операції
                );

                // Додаємо до списку транзакцій
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    private static Double parseNullableDouble(String value) {
        return "—".equals(value) ? null : Double.parseDouble(value);
    }
}
