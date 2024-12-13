package converter;

import converter.dto.Cashew;
import converter.dto.UkrsibOnline;

import java.util.ArrayList;
import java.util.List;

public class UkrsibToCashew {

    public static Cashew convertToCashew(UkrsibOnline operationDTO,String account) {
        Cashew cashew = new Cashew();

        cashew.setAccount(account);
        cashew.setDate(operationDTO.getDateTime()); // Перетворюємо дату і час
        cashew.setTitle(operationDTO.getDetails()); // Використовуємо details як title
        cashew.setAmount(operationDTO.getAmount()); // Сума операції
        cashew.setCurrency(operationDTO.getCurrency().equals("грн") ? "UAH" : ""); // Валюта
        cashew.setIncome(operationDTO.getAmount() > 0); // Перевіряємо, чи це дохід
        cashew.setCategoryName(""); // Категорію можна визначити за додатковими умовами
        cashew.setSubcategoryName(operationDTO.getCategory());
        cashew.setColor("");
        cashew.setNote(operationDTO.getDetails());
        return cashew;
    }

    public static List<Cashew> convertToCashewList(List<UkrsibOnline> operationDTOList, String account) {
        List<Cashew> cashewList = new ArrayList<>();
        for (UkrsibOnline operationDTO : operationDTOList) {
            cashewList.add(convertToCashew(operationDTO,account));
        }
        return cashewList;
    }
}