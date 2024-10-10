package org.example;

import org.example.dto.Cashew;
import org.example.dto.UkrsibOnline;

import java.util.List;

public class Runner {
    public static void main(String[] args) {
        // Шлях до вашого Excel-файлу
        String filePath = "/mnt/data/Список операцій.xlsx";

        // Читаємо Excel-файл
        List<UkrsibOnline> operationDTOList = ExcelReader.readExcel(filePath);

        // Конвертуємо в Cashew
        List<Cashew> cashewList = UkrsibToCashew.convertToCashewList(operationDTOList);

        // Виводимо конвертовані дані
        for (Cashew cashew : cashewList) {
            System.out.println(cashew);
        }
    }
}


