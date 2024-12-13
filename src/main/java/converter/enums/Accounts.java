package converter.enums;

import lombok.Getter;

@Getter
public enum Accounts {
    black_dasha("Black Dasha","Dasha","Black"),
    cash("Готівка","",""),
    black_mysha("Black Mysha","Mysha","Black"),
    white_mysha("White Mysha","Mysha","White"),
    mono_usd("mono USD","Mysha","USD"),
    ukrsib_bis_mysha("Ukrsib Business Mysha","Mysha",""),
    ukrsib_mysha("UkrSib Mysha","Mysha","UkrSib Mysha"),
    white_dasha("White Dasha","Dasha","White"),
    ukrsib_bis_dasha("Ukrsib Business Dasha","Dasha",""),
    ukrsib_dasha("Ukrsib Dasha","Dasha","Ukrsib Dasha"),
    reserve_fund("Резервний фонд","",""),
    travel("На подорожі","",""),
    jar_tv("Jar TV","",""),
    ;
    @Getter
    private final String description;
    private final String name;
    private final String cardName;

    Accounts(String description, String name, String cardName) {
        this.description = description;
        this.name = name;
        this.cardName = cardName;
    }
}
