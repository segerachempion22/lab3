package billing;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import exception.DuplicateSubscriptionException;
import exception.InsufficientFundsException;
import exception.InvalidEmailException;
import exception.PromoCodeExpiredException;
import exception.SubscriptionNotFoundException;
import model.ATV;
import servise.NotificationService;

public final class ATVClub {
  private final Map<String, ATV> database = new HashMap<>();
  private final NotificationService notificationService;
  private static int totalOrdersCount;
  private static BigDecimal totalRevenue = new BigDecimal("0.00");

  public ATVClub(NotificationService notificationService) {
    this.notificationService = Objects.requireNonNull(notificationService);
  }

  private String validateEmail(String email) {
    if (email == null || !email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}")) {
      throw new InvalidEmailException("Некорректный email: " + email);
    }
    return email.toLowerCase(Locale.ROOT);
  }

  public void registerRental(String email, ATV rental) throws DuplicateSubscriptionException {
    String key = validateEmail(email);
    if (rental == null) {
      throw new IllegalArgumentException("Аренда не должна быть null");
    }
    if (database.containsKey(key) || database.containsValue(rental)) {
      throw new DuplicateSubscriptionException("Email или аренда уже зарегистрированы: " + email);
    }
    database.put(key, rental);
  }

  public ATV findRental(String email) throws SubscriptionNotFoundException {
    ATV rental = database.get(validateEmail(email));
    if (rental == null) {
      throw new SubscriptionNotFoundException("Аренда для " + email + " не найдена в ATVClub");
    }
    return rental;
  }

  public void processPaymentByEmail(String email) throws SubscriptionNotFoundException,
      PromoCodeExpiredException, InsufficientFundsException {
    String key = validateEmail(email);
    ATV rental = findRental(key);
    BigDecimal price = rental.calculateBillingPrice();
    rental.debit(price);
    totalOrdersCount++;
    totalRevenue = totalRevenue.add(price);
    notificationService.sendNotification("Оплачена аренда: " + price + " руб.", key);
  }

  public int getRegisteredCount() {
    return database.size();
  }

  public static int getTotalOrdersCount() {
    return totalOrdersCount;
  }

  public static BigDecimal getTotalRevenue() {
    return totalRevenue;
  }

  public static void printGlobalStats() {
    System.out.println("Оплат: " + totalOrdersCount + "; выручка: " + totalRevenue + " руб.");
  }
}
