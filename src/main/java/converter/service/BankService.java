package converter.service;

import converter.dto.Cashew;
import converter.enums.Accounts;

import java.util.List;

public interface BankService {
    List<Cashew> getMonoTransactions(Accounts account);
    List<Cashew> getUkrsibTransactions(Accounts account);
    void exportAllTransactions(String outputPath);
}
