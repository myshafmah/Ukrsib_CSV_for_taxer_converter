package converter;

import converter.config.ServiceContainer;
import converter.service.BankService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting application");

        try {
            // Отримуємо сервіс з DI контейнера
            BankService bankService = ServiceContainer.getBankService();

            // Експортуємо всі транзакції
            bankService.exportAllTransactions(null); // null = використовуємо шлях за замовчуванням

            logger.info("Application completed successfully");
        } catch (Exception e) {
            logger.error("Application error", e);
        }
    }
}
