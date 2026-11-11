package servise.impl;

import servise.NotificationService;

public final class EmailNotification implements NotificationService {
  @Override
  public void sendNotification(String message, String recipient) {
    System.out.println("[EMAIL] На адрес " + recipient + " отправлено письмо: " + message);
  }
}
