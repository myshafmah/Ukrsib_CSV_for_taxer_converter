package converter;

import converter.dto.Cashew;
import converter.dto.MonoCSV;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CashewToMonoCSVConverter {

    public static Cashew monoCSVToCashew(MonoCSV monoCSV, String account) {
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
        return cashew;
    }

    public static List<Cashew> convertToCashewList(List<MonoCSV> operationDTOList, String account) {
        List<Cashew> cashewList = new ArrayList<>();
        for (MonoCSV operationDTO : operationDTOList) {
            cashewList.add(monoCSVToCashew(operationDTO, account));
        }
        return cashewList;
    }
}