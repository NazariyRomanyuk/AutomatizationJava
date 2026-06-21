package org.example;

public class PaymentService {
    private final ProductService productService;
    private final BankAccountService bankAccountService;
    private final NotificationService notificationService;

    public PaymentService(ProductService productService, BankAccountService bankAccountService, NotificationService notificationService) {
        this.productService = productService;
        this.bankAccountService = bankAccountService;
        this.notificationService = notificationService;
    }

    public void buyProduct(long productId, long accountId, int amount) {
        ProductModel product = productService.getProductById(productId);
        double amountToWithdraw = amount * product.getPrice();
        try {
            productService.takeProductFromWarehouse(productId, amount);
            try {
                bankAccountService.withdrawFromBalance(accountId, amountToWithdraw);
                notificationService.sendSuccessNotification(product.getName(), amount, product.getPrice());
            }
            catch (IllegalArgumentException e) {
                productService.addProductToWarehouse(productId, amount);
                notificationService.sendFailureNotification(e.getMessage());
            }
        }
        catch (IllegalArgumentException e) {
            notificationService.sendFailureNotification(e.getMessage());
        }

    }
}
