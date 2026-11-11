package exception;

public class DuplicateSubscriptionException extends Exception {
  private static final long serialVersionUID = 1L;

  public DuplicateSubscriptionException(String message) {
    super(message);
  }
}
