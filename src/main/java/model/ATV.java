package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import exception.InsufficientFundsException;
import exception.InvalidSubscriptionException;
import exception.PromoCodeExpiredException;

public abstract class ATV {
  private final String owner;
  private final BigDecimal basePrice;
  private final String tariff;
  private final List<GearRental> appliedGear = new ArrayList<>();
  private final List<PromoCode> appliedPromos = new ArrayList<>();
  private BigDecimal balance;

  protected ATV(String owner, BigDecimal basePrice, BigDecimal balance) {
    this(owner, basePrice, "Стандарт", balance);
  }

  protected ATV(String owner, BigDecimal basePrice, String tariff, BigDecimal balance) {
    if (owner == null || owner.isBlank() || basePrice == null || basePrice.signum() <= 0) {
      throw new InvalidSubscriptionException("Нужны имя владельца и положительная цена аренды");
    }
    if (balance == null || balance.signum() < 0) {
      throw new InvalidSubscriptionException("Баланс не может быть отрицательным");
    }
    if (tariff == null || !(tariff.equals("Стандарт") || tariff.equals("Спорт")
        || tariff.equals("Monster-Track"))) {
      throw new InvalidSubscriptionException("Неизвестный тариф аренды");
    }
    this.owner = owner.strip();
    this.basePrice = basePrice;
    this.tariff = tariff;
    this.balance = balance;
  }

  public String getOwner() {
    return owner;
  }

  public BigDecimal getBasePrice() {
    return basePrice;
  }

  public String getTariff() {
    return tariff;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public List<GearRental> getAppliedGear() {
    return List.copyOf(appliedGear);
  }

  public List<PromoCode> getAppliedPromos() {
    return List.copyOf(appliedPromos);
  }

  public void addGear(ProtectiveGear gear) {
    addGear(gear, 1);
  }

  public void addGear(ProtectiveGear gear, int hours) {
    if (gear == null || hours <= 0) {
      throw new IllegalArgumentException("Нужны экипировка и положительное число часов");
    }
    appliedGear.add(new GearRental(gear, hours));
  }

  public void addPromo(PromoCode promo) {
    if (promo == null) {
      throw new IllegalArgumentException("Промокод не должен быть null");
    }
    for (PromoCode current : appliedPromos) {
      if (current.getCode().equalsIgnoreCase(promo.getCode())) {
        throw new IllegalArgumentException("Промокод уже добавлен");
      }
    }
    appliedPromos.add(promo);
  }

  protected BigDecimal priceWithGear() {
    BigDecimal coefficient = switch (tariff) {
      case "Стандарт" -> BigDecimal.ONE;
      case "Спорт" -> new BigDecimal("1.5");
      case "Monster-Track" -> new BigDecimal("2.1");
      default -> throw new InvalidSubscriptionException("Неизвестный тариф");
    };
    BigDecimal price = basePrice.multiply(coefficient);
    for (GearRental rental : appliedGear) {
      price = price.add(rental.gear().getHourlyPrice().multiply(BigDecimal.valueOf(rental.hours())));
    }
    return price;
  }

  protected BigDecimal applyPromos(BigDecimal price) throws PromoCodeExpiredException {
    for (PromoCode promo : appliedPromos) {
      promo.validateOn(LocalDate.now());
      price = price.multiply(BigDecimal.ONE.subtract(promo.getDiscountPercent().movePointLeft(2)));
    }
    return price;
  }

  protected BigDecimal round(BigDecimal price) {
    return price.setScale(2, RoundingMode.HALF_UP);
  }

  public void debit(BigDecimal amount) throws InsufficientFundsException {
    if (amount == null || amount.signum() < 0) {
      throw new IllegalArgumentException("Некорректная сумма списания");
    }
    if (balance.compareTo(amount) < 0) {
      throw new InsufficientFundsException("Недостаточно средств у " + owner);
    }
    balance = balance.subtract(amount);
  }

  public abstract BigDecimal calculateBillingPrice() throws PromoCodeExpiredException;

  public record GearRental(ProtectiveGear gear, int hours) {}
}
