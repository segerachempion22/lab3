package exception;

public class InvalidSubscriptionException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InvalidSubscriptionException(String message) {
    super(message);
  }
}
