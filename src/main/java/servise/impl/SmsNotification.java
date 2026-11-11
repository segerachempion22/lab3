package servise.impl;

import servise.NotificationService;

public final class SmsNotification implements NotificationService {
  @Override
  public void sendNotification(String message, String recipient) {
    System.out.println("[SMS] На номер " + recipient + " отправлено сообщение: " + message);
  }
}
