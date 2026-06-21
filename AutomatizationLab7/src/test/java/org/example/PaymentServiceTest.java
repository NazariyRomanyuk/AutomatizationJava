package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @Mock private ProductService productService;
    @Mock private BankAccountService bankAccountService;
    @Mock private NotificationService notificationService;
    @InjectMocks private PaymentService paymentService;


    @Test
    void buyProductSucceedsOnSuccessfulPurchase() {
        ProductModel product = new ProductModel(1L, "Bottled Water", 5, 50);
        when(productService.getProductById(1L)).thenReturn(product);
        paymentService.buyProduct(1L, 5L, 5);
        verify(productService, times(1)).takeProductFromWarehouse(1L, 5);
        verify(bankAccountService, times(1)).withdrawFromBalance(5L, 25);
        verify(notificationService).sendSuccessNotification("Bottled Water", 5, 5);
        verify(notificationService, never()).sendFailureNotification(anyString());
    }

    @Test
    void buyProductFailsOnInsufficientStock() {
        ProductModel product = new ProductModel(1L, "Bottled Water", 5, 50);
        when(productService.getProductById(1L)).thenReturn(product);
        doThrow(new IllegalArgumentException("Not enough Bottled Water left in stock!"))
                .when(productService).takeProductFromWarehouse(1L, 55);
        paymentService.buyProduct(1L, 5L, 55);
        verify(bankAccountService, never()).withdrawFromBalance(anyLong(), anyInt());
        verify(notificationService).sendFailureNotification("Not enough Bottled Water left in stock!");
    }

    @Test
    void buyProductFailsOnInsufficientBalance() {
        ProductModel product = new ProductModel(1L, "Bottled Water", 5, 50);
        when(productService.getProductById(1L)).thenReturn(product);
        doThrow(new IllegalArgumentException("Not enough money left!"))
                .when(bankAccountService).withdrawFromBalance(5L, 25);
        paymentService.buyProduct(1L, 5L, 5);
        verify(productService).addProductToWarehouse(1L, 5);
        verify(notificationService).sendFailureNotification("Not enough money left!");
    }

}
