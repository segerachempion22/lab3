package servise;

public final class ConsoleNotificationService implements NotificationService {
  @Override
  public void sendNotification(String message, String recipient) {
    System.out.println("[CONSOLE] " + recipient + ": " + message);
  }
}
