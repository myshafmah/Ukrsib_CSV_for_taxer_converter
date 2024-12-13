package converter;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import converter.dto.MonoCSV;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ConverterApplication {

    public List<MonoCSV> dynamicCSVFileReader(String name, String cardColor) {
        String userHome = System.getProperty("user.home"); // Отримуємо шлях до поточного користувача
        String downloadsFolder = userHome + "\\Downloads\\report\\mono" + "\\" + name + "\\" + cardColor + "\\"; // Шлях до папки Downloads

        // Створюємо шаблон для пошуку файлу
        String filePrefix = "report_";
        String fileExtension = ".csv";

        // Шукаємо файл за шаблоном у теці Downloads
        Optional<File> file = findLatestFile(downloadsFolder, filePrefix, fileExtension);

        // Якщо файл знайдений, читаємо його вміст
        if (file.isPresent()) {
            System.out.println("Файл знайдено: " + file.get().getName());
            try {
                // Парсимо файл CSV і повертаємо список MonoCSV
                return parseCSVFile(file.get());
            } catch (IOException | CsvException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("\n" + "Файл не знайдено. " + downloadsFolder + filePrefix + fileExtension);
        }
        return new ArrayList<>();
    }

    // Метод для парсингу CSV-файлу в об'єкти MonoCSV
    private List<MonoCSV> parseCSVFile(File csvFile) throws IOException, CsvException {
        List<MonoCSV> transactions = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8))) {
            List<String[]> columns = reader.readAll();

            for (String[] column : columns) {
                // Пропускаємо заголовки (перший рядок)
                if (column[0].equals("Дата i час операції")) {
                    continue;
                }

                // Створюємо DTO для кожного рядка CSV
                MonoCSV transaction = new MonoCSV(column[0],                               // Дата і час операції
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
        }
        return transactions;
    }

    // Метод для пошуку останнього зміненого файлу за шаблоном
    public static Optional<File> findLatestFile(String directoryPath, String filePrefix, String fileExtension) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.startsWith(filePrefix) && name.endsWith(fileExtension));

        if (files == null || files.length == 0) {
            return Optional.empty(); // Якщо файли не знайдено
        }

        // Знаходимо останній змінений файл
        File latestFile = files[0];
        for (File file : files) {
            if (file.lastModified() > latestFile.lastModified()) {
                latestFile = file;
            }
        }
        return Optional.of(latestFile);
    }

}
