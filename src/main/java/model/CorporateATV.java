package model;

import java.math.BigDecimal;
import exception.InvalidSubscriptionException;

public final class CorporateATV extends ATV {
  private final int activeVehiclesCount;

  public CorporateATV(String owner, BigDecimal basePrice, String tariff,
      BigDecimal balance, int activeVehiclesCount) {
    super(owner, basePrice, tariff, balance);
    if (activeVehiclesCount < 1) {
      throw new InvalidSubscriptionException("Количество квадроциклов должно быть положительным");
    }
    this.activeVehiclesCount = activeVehiclesCount;
  }

  public int getActiveVehiclesCount() {
    return activeVehiclesCount;
  }

  @Override
  public void addPromo(PromoCode promo) {
    if (promo == null) {
      throw new IllegalArgumentException("Промокод не должен быть null");
    }
    throw new IllegalArgumentException("Корпоративной аренде промокоды недоступны");
  }

  @Override
  public BigDecimal calculateBillingPrice() {
    return round(priceWithGear().multiply(BigDecimal.valueOf(activeVehiclesCount)));
  }
}
