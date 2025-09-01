package converter.converter.impl;

import converter.converter.DataConverter;
import converter.dto.Cashew;
import converter.dto.UkrsibOnline;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class UkrsibToCashewConverterImpl implements DataConverter<UkrsibOnline, Cashew> {
    private static final Logger logger = LogManager.getLogger(UkrsibToCashewConverterImpl.class);

    @Override
    public Cashew convert(UkrsibOnline operationDTO, String account) {
        logger.debug("Converting UkrsibOnline to Cashew for account: {}", account);
        Cashew cashew = new Cashew();

        cashew.setAccount(account);
        cashew.setDate(operationDTO.getDateTime()); // Перетворюємо дату і час
        cashew.setTitle(operationDTO.getDetails()); // Використовуємо details як title
        cashew.setAmount(operationDTO.getAmount()); // Сума операції
        cashew.setCurrency(operationDTO.getCurrency().equals("грн") ? "UAH" : operationDTO.getCurrency()); // Валюта
        cashew.setIncome(operationDTO.getAmount() > 0); // Перевіряємо, чи це дохід
        cashew.setCategoryName(""); // Категорію можна визначити за додатковими умовами
        cashew.setSubcategoryName(operationDTO.getCategory());
        cashew.setColor("");
        cashew.setNote(operationDTO.getDetails());

        // Додаткова логіка визначення категорій може бути реалізована тут

        return cashew;
    }

    @Override
    public List<Cashew> convertList(List<UkrsibOnline> operationDTOList, String account) {
        logger.info("Converting {} UkrsibOnline records to Cashew format for account: {}",
                operationDTOList.size(), account);
        return operationDTOList.stream()
                .map(dto -> convert(dto, account))
                .collect(Collectors.toList());
    }
}
