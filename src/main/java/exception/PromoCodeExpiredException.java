package exception;

public class PromoCodeExpiredException extends Exception {
  private static final long serialVersionUID = 1L;

  public PromoCodeExpiredException(String message) {
    super(message);
  }
}
