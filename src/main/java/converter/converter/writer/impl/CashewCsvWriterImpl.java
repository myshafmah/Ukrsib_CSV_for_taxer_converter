package converter.writer.impl;

import converter.dto.Cashew;
import converter.writer.DataWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CashewCsvWriterImpl implements DataWriter<Cashew> {
    private static final Logger logger = LogManager.getLogger(CashewCsvWriterImpl.class);

    @Override
    public void write(List<Cashew> data, String outputPath) throws IOException {
        logger.info("Writing {} Cashew records to CSV: {}", data.size(), outputPath);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {

            // Write header
            writer.write("Date,Amount,Category,Title,Note,Account\n");

            // Write data
            for (Cashew cashew : data) {
                writer.write(
                        cashew.getDate() + "," +
                                cashew.getAmount() + "," +
                                (cashew.getSubcategoryName().isEmpty() ? cashew.getCategoryName() : cashew.getSubcategoryName()) + ",\"" +
                                escapeCSV(cashew.getTitle()) + "\",\"" +
                                escapeCSV(cashew.getNote()) + "\"," +
                                cashew.getAccount() +
                                "\n");
            }

            logger.info("Successfully wrote data to: {}", outputPath);
        } catch (IOException e) {
            logger.error("Error writing Cashew data to CSV", e);
            throw e;
        }
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        return value.replace("\"", "\"\"");
    }
}
