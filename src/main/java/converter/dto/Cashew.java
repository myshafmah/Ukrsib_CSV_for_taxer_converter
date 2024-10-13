package converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cashew {
    private String account;
    private double amount;
    private String currency;
    private String title;
    private String note;
    private String date;
    private boolean income;
    private String type;
    private String categoryName;
    private String subcategoryName;
    private String color;
    private String icon;
    private String emoji;
    private String budget;
    private String objective;
}
