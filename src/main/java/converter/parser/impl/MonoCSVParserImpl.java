package converter.parser.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import converter.dto.MonoCSV;
import converter.parser.GenericParser;
import converter.service.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MonoCSVParserImpl implements GenericParser<MonoCSV> {
    private static final Logger logger = LogManager.getLogger(MonoCSVParserImpl.class);
    private final FileService fileService;

    public MonoCSVParserImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public List<MonoCSV> parse(File csvFile) throws IOException, CsvException {
        logger.info("Parsing Mono CSV file: {}", csvFile.getName());
        List<MonoCSV> transactions = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8))) {
            List<String[]> columns = reader.readAll();

            for (String[] column : columns) {
                // Пропускаємо заголовки (перший рядок)
                if (column[0].equals("Дата i час операції")) {
                    continue;
                }

                try {
                    // Створюємо DTO для кожного рядка CSV
                    MonoCSV transaction = new MonoCSV(
                            column[0],                               // Дата і час операції
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
                } catch (NumberFormatException e) {
                    logger.error("Error parsing numeric value in row: {}", String.join(", ", column), e);
                    // Можна продовжувати, пропустивши проблемний рядок
                }
            }
        }
        logger.info("Successfully parsed {} transactions from Mono CSV", transactions.size());
        return transactions;
    }

    @Override
    public List<MonoCSV> parseFromPath(String filePath) throws IOException, CsvException {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("File does not exist: {}", filePath);
            throw new IOException("File does not exist: " + filePath);
        }
        return parse(file);
    }

    public List<MonoCSV> dynamicCSVFileReader(String name, String cardColor) {
        String downloadsFolder = fileService.getUserHomeDirectory() + "\\Downloads\\report\\mono" + "\\" + name + "\\" + cardColor + "\\";
        String filePrefix = "report_";
        String fileExtension = ".csv";

        Optional<File> file = fileService.findLatestFile(downloadsFolder, filePrefix, fileExtension);

        if (file.isPresent()) {
            logger.info("Found Mono CSV file: {}", file.get().getName());
            try {
                return parse(file.get());
            } catch (Exception e) {
                logger.error("Error parsing Mono CSV file", e);
            }
        } else {
            logger.error("Mono CSV file not found in: {}", downloadsFolder);
        }
        return new ArrayList<>();
    }
}
