package org.example;

public class NotificationService {
    public void sendSuccessNotification(String productName, int amount, double price) {
        System.out.println(amount + " " + productName + " for $" + price + " have been bought successfully.");
    }
    public void sendFailureNotification(String errorMessage) {
        System.out.println("Failed to buy product:\n" + errorMessage);
    }
}
