package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class DynamicFileReader {

    public static void main(String[] args) {
        String userHome = System.getProperty("user.home"); // Отримуємо шлях до поточного користувача
        String downloadsFolder = userHome + "\\Downloads"; // Шлях до папки Downloads

        // Створюємо шаблон для пошуку файлу
        String filePrefix = "report_";
        String fileExtension = ".csv";

        // Шукаємо файл за шаблоном у теці Downloads
        Optional<File> file = findLatestFile(downloadsFolder, filePrefix, fileExtension);

        // Якщо файл знайдений, читаємо його вміст
        if (file.isPresent()) {
            System.out.println("Файл знайдено: " + file.get().getName());
            try {
                // Читаємо вміст файлу
                Files.lines(file.get().toPath()).forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Файл не знайдено.");
        }
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
