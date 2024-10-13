package converter;

import converter.dto.Cashew;
import converter.dto.UkrsibOnline;

import java.util.ArrayList;
import java.util.List;

public class UkrsibToCashew {

    public static Cashew convertToCashew(UkrsibOnline operationDTO) {
        Cashew cashew = new Cashew();

        cashew.setDate(operationDTO.getDateTime()); // Перетворюємо дату і час
        cashew.setTitle(operationDTO.getDetails()); // Використовуємо details як title
        cashew.setAmount(operationDTO.getAmountInCardCurrency()); // Сума операції
        cashew.setCurrency(operationDTO.getCurrency()); // Валюта
        cashew.setIncome(operationDTO.getAmountInCardCurrency() > 0); // Перевіряємо, чи це дохід
        cashew.setCategoryName("Unknown"); // Категорію можна визначити за додатковими умовами
        cashew.setSubcategoryName("Unknown");
        cashew.setColor("Unknown");

        return cashew;
    }

    public static List<Cashew> convertToCashewList(List<UkrsibOnline> operationDTOList) {
        List<Cashew> cashewList = new ArrayList<>();
        for (UkrsibOnline operationDTO : operationDTOList) {
            cashewList.add(convertToCashew(operationDTO));
        }
        return cashewList;
    }
}
