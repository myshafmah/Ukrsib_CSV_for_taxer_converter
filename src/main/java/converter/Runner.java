package converter;

import converter.dto.Cashew;
import converter.dto.MonoCSV;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Runner {
    public static void main(String[] args) {
        String userHome = System.getProperty("user.home"); // Отримуємо шлях до поточного користувача
        String downloadsFolder = userHome + "\\Downloads"; // Шлях до папки Downloads
        // Шлях до вашого Excel-файлу
//        String filePath = "c:\\Users\\m.fedoryshyn\\Downloads\\Список операцій.xlsx";

        // Читаємо Excel-файл
//        List<UkrsibOnline> operationDTOList = ExcelReader.readExcel(filePath);
        List<MonoCSV> monoCSVList = new ConverterApplication().dynamicFileReader();
        // Конвертуємо в Cashew
//        List<Cashew> cashewList = UkrsibToCashew.convertToCashewList(operationDTOList);
        List<Cashew> cashewList = CashewToMonoCSVConverter.convertToCashewList(monoCSVList,"Black Mysha");
        // Виводимо конвертовані дані

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(downloadsFolder + "\\Cashew.csv"), StandardCharsets.UTF_8))) {
            writer.write("Date,Amount,Category,Title,Note,Account \n");
            for (Cashew cashew : cashewList) {
                writer.write(
                        cashew.getDate() + "," +
                                cashew.getAmount() + "," +
                                cashew.getCategoryName() + "," +
                                cashew.getTitle() + "," +
                                cashew.getNote() + "," +
                                cashew.getAccount() +
                                "\n");
            }
            System.out.println("Conversion completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



