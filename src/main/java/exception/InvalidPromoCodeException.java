package exception;

public class InvalidPromoCodeException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InvalidPromoCodeException(String message) {
    super(message);
  }
}
