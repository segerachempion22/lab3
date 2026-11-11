import java.math.BigDecimal;
import java.time.LocalDate;
import billing.ATVClub;
import exception.*;
import model.*;
import servise.impl.EmailNotification;

public final class Main {
  private Main() {}

  public static void main(String[] args) {
    ATVClub club = new ATVClub(new EmailNotification());
    ProtectiveGear helmet = new ProtectiveGear("Шлем", new BigDecimal("100"));
    ProtectiveGear suit = new ProtectiveGear("Защитный костюм", new BigDecimal("200"));
    UserATV user = new UserATV("Иван", new BigDecimal("1000"), "Спорт", new BigDecimal("5000"));
    CorporateATV company = new CorporateATV("ТурКлуб", new BigDecimal("1000"),
        "Monster-Track", new BigDecimal("50000"), 5);
    UserATV lowBalance = new UserATV("Петр", new BigDecimal("1000"), BigDecimal.ZERO);
    for (ATV rental : new ATV[] {user, company, lowBalance}) {
      rental.addGear(helmet, 2);
      rental.addGear(suit, 2);
    }
    user.addPromo(new PromoCode("ATV10", new BigDecimal("10"), LocalDate.now().plusDays(1)));
    try {
      club.registerRental("ivan@example.com", user);
      club.registerRental("club@example.com", company);
      club.registerRental("petr@example.com", lowBalance);
      club.processPaymentByEmail("ivan@example.com");
      club.processPaymentByEmail("club@example.com");
    } catch (DuplicateSubscriptionException | SubscriptionNotFoundException
        | PromoCodeExpiredException | InsufficientFundsException e) {
      System.out.println("Ошибка демонстрации: " + e.getMessage());
    }
    try {
      club.processPaymentByEmail("fake@mail.com");
    } catch (SubscriptionNotFoundException | PromoCodeExpiredException | InsufficientFundsException e) {
      System.out.println("Ожидаемый отказ: " + e.getMessage());
    } finally {
      System.out.println("Поиск завершен, программа продолжает работу");
    }
    try {
      new UserATV("Иван", new BigDecimal("-1"), BigDecimal.ZERO);
    } catch (InvalidSubscriptionException e) {
      System.out.println("Ожидаемый отказ: " + e.getMessage());
    }
    try {
      club.registerRental("IVAN@example.com", lowBalance);
    } catch (DuplicateSubscriptionException e) {
      System.out.println("Ожидаемый отказ: " + e.getMessage());
    }
    try {
      club.registerRental("invalid-email", lowBalance);
    } catch (InvalidEmailException | DuplicateSubscriptionException e) {
      System.out.println("Ожидаемый отказ: " + e.getMessage());
    }
    try {
      new PromoCode("BAD CODE", new BigDecimal("101"), LocalDate.now());
    } catch (InvalidPromoCodeException e) {
      System.out.println("Ожидаемый отказ: " + e.getMessage());
    }
    try {
      club.processPaymentByEmail("petr@example.com");
    } catch (SubscriptionNotFoundException | PromoCodeExpiredException | InsufficientFundsException e) {
      System.out.println("Ожидаемый отказ: " + e.getMessage());
    }
    lowBalance.addPromo(new PromoCode("OLD", BigDecimal.TEN, LocalDate.now().minusDays(1)));
    try {
      club.processPaymentByEmail("petr@example.com");
    } catch (SubscriptionNotFoundException | PromoCodeExpiredException | InsufficientFundsException e) {
      System.out.println("Ожидаемый отказ: " + e.getMessage());
    }
    ATVClub.printGlobalStats();
    System.out.println("Все сценарии завершены без аварийного выхода");
  }
}
