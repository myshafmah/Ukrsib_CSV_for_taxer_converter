package converter.service.impl;

import converter.converter.DataConverter;
import converter.dto.Cashew;
import converter.dto.MonoCSV;
import converter.dto.UkrsibOnline;
import converter.enums.Accounts;
import converter.parser.GenericParser;
import converter.parser.impl.MonoCSVParserImpl;
import converter.parser.impl.UkrsibExcelParserImpl;
import converter.service.BankService;
import converter.service.FileService;
import converter.writer.DataWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BankServiceImpl implements BankService {
    private static final Logger logger = LogManager.getLogger(BankServiceImpl.class);

    private final GenericParser<MonoCSV> monoParser;
    private final GenericParser<UkrsibOnline> ukrsibParser;
    private final DataConverter<MonoCSV, Cashew> monoConverter;
    private final DataConverter<UkrsibOnline, Cashew> ukrsibConverter;
    private final DataWriter<Cashew> cashewWriter;
    private final FileService fileService;

    public BankServiceImpl(
            GenericParser<MonoCSV> monoParser,
            GenericParser<UkrsibOnline> ukrsibParser,
            DataConverter<MonoCSV, Cashew> monoConverter,
            DataConverter<UkrsibOnline, Cashew> ukrsibConverter,
            DataWriter<Cashew> cashewWriter,
            FileService fileService) {
        this.monoParser = monoParser;
        this.ukrsibParser = ukrsibParser;
        this.monoConverter = monoConverter;
        this.ukrsibConverter = ukrsibConverter;
        this.cashewWriter = cashewWriter;
        this.fileService = fileService;
    }

    @Override
    public List<Cashew> getMonoTransactions(Accounts account) {
        logger.info("Getting Mono transactions for account: {}", account.getDescription());
        try {
            List<MonoCSV> transactions = ((MonoCSVParserImpl)monoParser).dynamicCSVFileReader(
                    account.getName(), account.getCardName());
            return monoConverter.convertList(transactions, account.getDescription());
        } catch (Exception e) {
            logger.error("Error getting Mono transactions", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Cashew> getUkrsibTransactions(Accounts account) {
        logger.info("Getting Ukrsib transactions for account: {}", account.getDescription());
        try {
            List<UkrsibOnline> transactions = ((UkrsibExcelParserImpl)ukrsibParser).dynamicExcelFileReader(
                    account.getName(), account.getCardName());
            return ukrsibConverter.convertList(transactions, account.getDescription());
        } catch (Exception e) {
            logger.error("Error getting Ukrsib transactions", e);
            return new ArrayList<>();
        }
    }

    @Override
    public void exportAllTransactions(String outputPath) {
        logger.info("Exporting all transactions to: {}", outputPath);
        String downloadsFolder = fileService.getDownloadsPath();
        String fileName = downloadsFolder + "\\Cashew_" + new Date().getTime() + ".csv";

        List<Cashew> allTransactions = new ArrayList<>();

        // Додаємо Mono транзакції
        allTransactions.addAll(getMonoTransactions(Accounts.white_dasha));
        allTransactions.addAll(getMonoTransactions(Accounts.black_dasha));
        allTransactions.addAll(getMonoTransactions(Accounts.white_mysha));
        allTransactions.addAll(getMonoTransactions(Accounts.black_mysha));
        allTransactions.addAll(getMonoTransactions(Accounts.mono_usd));

        // Додаємо Ukrsib транзакції
        allTransactions.addAll(getUkrsibTransactions(Accounts.ukrsib_mysha));
        allTransactions.addAll(getUkrsibTransactions(Accounts.ukrsib_dasha));

        try {
            cashewWriter.write(allTransactions, fileName);
            logger.info("Successfully exported {} transactions to {}", allTransactions.size(), fileName);
        } catch (IOException e) {
            logger.error("Error exporting transactions to CSV", e);
        }
    }
}
