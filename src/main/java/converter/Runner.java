package converter;


import converter.dto.Cashew;
import converter.dto.MonoCSV;
import converter.dto.UkrsibOnline;
import converter.enums.Accounts;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class Runner {
    public static void main(String[] args) {
        // Шлях до вашого Excel-файлу
        String userHome = System.getProperty("user.home"); // Отримуємо шлях до поточного користувача
        String downloadsFolder = userHome + "\\Downloads"; // Шлях до папки Downloads
        // Виводимо конвертовані дані
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(downloadsFolder + "\\Cashew_" + new Date().getTime() + ".csv"), StandardCharsets.UTF_8))) {
            writer.write("Date,Amount,Category,Title,Note,Account \n");
            // Читаємо Excel-файл

            // Конвертуємо в Cashew
            monoWriter(monoReader(Accounts.white_dasha), writer, Accounts.white_dasha);
            monoWriter(monoReader(Accounts.black_dasha), writer, Accounts.black_dasha);
            monoWriter(monoReader(Accounts.white_mysha), writer, Accounts.white_mysha);
            monoWriter(monoReader(Accounts.black_mysha), writer, Accounts.black_mysha);
            monoWriter(monoReader(Accounts.mono_usd), writer, Accounts.mono_usd);
            ukrsibWriter(ukrsibOnlineReader(Accounts.ukrsib_mysha), writer, Accounts.ukrsib_mysha);
            ukrsibWriter(ukrsibOnlineReader(Accounts.ukrsib_dasha), writer, Accounts.ukrsib_dasha);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<MonoCSV> monoReader(Accounts account) {
        return new ConverterApplication().dynamicCSVFileReader(account.getName(), account.getCardName());
    }

    private static List<UkrsibOnline> ukrsibOnlineReader(Accounts account) {
        return new ExcelReader().dynamicExcelFileReader(account.getName(), account.getCardName());
    }

    private static void monoWriter(List<MonoCSV> monoCSVList, BufferedWriter writer, Accounts accounts) throws IOException {
        List<Cashew> cashewList = CashewToMonoCSVConverter.convertToCashewList(monoCSVList, accounts.getDescription());
        for (Cashew cashew : cashewList) {
            writer.write(
                    cashew.getDate() + "," +
                            cashew.getAmount() + "," +
                            cashew.getCategoryName() + ",\"" +
                            cashew.getTitle() + "\",\"" +
                            cashew.getNote() + "\"," +
                            cashew.getAccount() +
                            "\n");
        }
        System.out.println(accounts.getDescription() + ": Conversion completed successfully.");
    }

    private static void ukrsibWriter(List<UkrsibOnline> ukrsibOnlineList, BufferedWriter writer, Accounts accounts) throws IOException {
        List<Cashew> cashewList = UkrsibToCashew.convertToCashewList(ukrsibOnlineList, accounts.getDescription());
        for (Cashew cashew : cashewList) {
            writer.write(
                    cashew.getDate() + "," +
                            cashew.getAmount() + "," +
                            (cashew.getSubcategoryName().equals("") ? cashew.getCategoryName() : cashew.getSubcategoryName()) + ",\"" +
                            cashew.getTitle() + "\",\"" +
                            cashew.getNote() + "\"," +
                            cashew.getAccount() +
                            "\n");
        }
        System.out.println(accounts.getDescription() + ": Conversion completed successfully.");
    }
}