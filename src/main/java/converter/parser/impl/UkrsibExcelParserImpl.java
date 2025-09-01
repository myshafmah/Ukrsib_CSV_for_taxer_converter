package converter.parser.impl;

import converter.dto.UkrsibOnline;
import converter.parser.GenericParser;
import converter.service.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class UkrsibExcelParserImpl implements GenericParser<UkrsibOnline> {
    private static final Logger logger = LogManager.getLogger(UkrsibExcelParserImpl.class);
    private final FileService fileService;

    public UkrsibExcelParserImpl(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public List<UkrsibOnline> parse(File excelFile) throws IOException {
        logger.info("Parsing Ukrsib Excel file: {}", excelFile.getName());
        List<UkrsibOnline> operations = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(excelFile);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Отримуємо перший лист
            int rowCount = sheet.getPhysicalNumberOfRows();

            // Пропускаємо перший рядок, якщо це заголовки
            for (int i = 1; i < rowCount; i++) {
                try {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

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
                } catch (Exception e) {
                    logger.error("Error parsing row {} in Excel file", i, e);
                    // Продовжуємо з наступним рядком
                }
            }
        }
        logger.info("Successfully parsed {} operations from Ukrsib Excel", operations.size());
        return operations;
    }

    @Override
    public List<UkrsibOnline> parseFromPath(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            logger.error("File does not exist: {}", filePath);
            throw new IOException("File does not exist: " + filePath);
        }
        return parse(file);
    }

    public List<UkrsibOnline> dynamicExcelFileReader(String name, String cardColor) {
        String downloadsFolder = fileService.getUserHomeDirectory() + "\\Downloads\\report\\ukrsib" + "\\" + name + "\\" + "Debet" + "\\";
        String filePrefix = "Список операцій по рахунку 26208810325732";
        String fileExtension = ".xlsx";

        Optional<File> file = fileService.findLatestFile(downloadsFolder, filePrefix, fileExtension);

        if (file.isPresent()) {
            logger.info("Found Ukrsib Excel file: {}", file.get().getName());
            try {
                return parse(file.get());
            } catch (Exception e) {
                logger.error("Error parsing Ukrsib Excel file", e);
            }
        } else {
            logger.error("Ukrsib Excel file not found in: {}", downloadsFolder);
        }
        return new ArrayList<>();
    }
}
