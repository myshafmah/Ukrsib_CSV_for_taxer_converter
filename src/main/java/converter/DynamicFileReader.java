package converter;

import converter.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.logging.Logger;

public class DynamicFileReader {

    private static final Logger logger = Logger.getLogger(DynamicFileReader.class.getName());

    public void dynamicFileReader() {
        String userHome = System.getProperty("user.home"); // Отримуємо шлях до поточного користувача
        String downloadsFolder = Paths.get(userHome, "Downloads").toString(); // Кросплатформений шлях

        // Створюємо шаблон для пошуку файлу
        String filePrefix = "report_";
        String fileExtension = ".csv";

        // Шукаємо файл за шаблоном у теці Downloads
        Optional<File> file = FileUtils.findLatestFile(downloadsFolder, filePrefix, fileExtension);

        // Якщо файл знайдений, читаємо його вміст
        if (file.isPresent()) {
            logger.info("Файл знайдено: " + file.get().getName());
            try {
                // Читаємо вміст файлу
                Files.lines(file.get().toPath()).forEach(line -> logger.fine(line));
            } catch (IOException e) {
                logger.severe("Помилка читання файлу: " + e.getMessage());
            }
        } else {
            logger.fine("Файл не знайдено у теці: " + downloadsFolder);
        }
    }
}