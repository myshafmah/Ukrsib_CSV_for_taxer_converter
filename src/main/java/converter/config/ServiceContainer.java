package converter.config;

import converter.converter.DataConverter;
import converter.converter.impl.MonoToCashewConverterImpl;
import converter.converter.impl.UkrsibToCashewConverterImpl;
import converter.dto.Cashew;
import converter.dto.MonoCSV;
import converter.dto.UkrsibOnline;
import converter.parser.GenericParser;
import converter.parser.impl.MonoCSVParserImpl;
import converter.parser.impl.UkrsibExcelParserImpl;
import converter.service.BankService;
import converter.service.FileService;
import converter.service.impl.BankServiceImpl;
import converter.service.impl.FileServiceImpl;
import converter.writer.DataWriter;
import converter.writer.impl.CashewCsvWriterImpl;

public class ServiceContainer {
    private static FileService fileService;
    private static GenericParser<MonoCSV> monoParser;
    private static GenericParser<UkrsibOnline> ukrsibParser;
    private static DataConverter<MonoCSV, Cashew> monoConverter;
    private static DataConverter<UkrsibOnline, Cashew> ukrsibConverter;
    private static DataWriter<Cashew> cashewWriter;
    private static BankService bankService;

    // Ініціалізація контейнера
    public static void initialize() {
        // Створюємо базові сервіси
        fileService = new FileServiceImpl();

        // Створюємо парсери
        monoParser = new MonoCSVParserImpl(fileService);
        ukrsibParser = new UkrsibExcelParserImpl(fileService);

        // Створюємо конвертери
        monoConverter = new MonoToCashewConverterImpl();
        ukrsibConverter = new UkrsibToCashewConverterImpl();

        // Створюємо writer
        cashewWriter = new CashewCsvWriterImpl();

        // Створюємо основний сервіс
        bankService = new BankServiceImpl(
                monoParser,
                ukrsibParser,
                monoConverter,
                ukrsibConverter,
                cashewWriter,
                fileService);
    }

    // Гетери для інстансів сервісів
    public static FileService getFileService() {
        if (fileService == null) initialize();
        return fileService;
    }

    public static GenericParser<MonoCSV> getMonoParser() {
        if (monoParser == null) initialize();
        return monoParser;
    }

    public static GenericParser<UkrsibOnline> getUkrsibParser() {
        if (ukrsibParser == null) initialize();
return ukrsibParser;
    }

    public static DataConverter<MonoCSV, Cashew> getMonoConverter() {
        if (monoConverter == null) initialize();
        return monoConverter;
    }

    public static DataConverter<UkrsibOnline, Cashew> getUkrsibConverter() {
        if (ukrsibConverter == null) initialize();
        return ukrsibConverter;
    }

    public static DataWriter<Cashew> getCashewWriter() {
        if (cashewWriter == null) initialize();
        return cashewWriter;
    }

    public static BankService getBankService() {
        if (bankService == null) initialize();
        return bankService;
    }
}
