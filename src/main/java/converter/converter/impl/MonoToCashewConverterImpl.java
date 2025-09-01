package converter.converter.impl;

import converter.converter.DataConverter;
import converter.dto.Cashew;
import converter.dto.MonoCSV;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class MonoToCashewConverterImpl implements DataConverter<MonoCSV, Cashew> {
    private static final Logger logger = LogManager.getLogger(MonoToCashewConverterImpl.class);

    @Override
    public Cashew convert(MonoCSV monoCSV, String account) {
        logger.debug("Converting MonoCSV to Cashew for account: {}", account);
        Cashew cashew = new Cashew();

        cashew.setAccount(account);
        cashew.setDate(StringUtils.substring(monoCSV.getDateTime(), 0, -3).replace(".", "/")); // Встановлюємо дату з MonoCSV
        cashew.setTitle(monoCSV.getDetails()); // Використовуємо details як title
        cashew.setAmount(monoCSV.getAmountInCardCurrency()); // Використовуємо суму в валюті картки
        cashew.setCurrency(monoCSV.getCurrency()); // Встановлюємо валюту
        cashew.setIncome(monoCSV.getAmountInCardCurrency() > 0); // Якщо сума позитивна, це дохід
        cashew.setCategoryName("Temporary"); // Категорію потрібно визначити додатково
        cashew.setSubcategoryName(""); // Підкатегорію потрібно визначити додатково
        cashew.setColor(""); // Колір можна визначити додатково
        cashew.setNote(monoCSV.getDetails());

        // Додаткова логіка визначення категорій може бути реалізована тут

        return cashew;
    }

    @Override
    public List<Cashew> convertList(List<MonoCSV> operationDTOList, String account) {
        logger.info("Converting {} MonoCSV records to Cashew format for account: {}",
                operationDTOList.size(), account);
        return operationDTOList.stream()
                .map(dto -> convert(dto, account))
                .collect(Collectors.toList());
    }
}
