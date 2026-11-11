package model;

import java.math.BigDecimal;
import exception.PromoCodeExpiredException;

public final class UserATV extends ATV {
  public UserATV(String owner, BigDecimal basePrice, BigDecimal balance) {
    super(owner, basePrice, balance);
  }

  public UserATV(String owner, BigDecimal basePrice, String tariff, BigDecimal balance) {
    super(owner, basePrice, tariff, balance);
  }

  @Override
  public BigDecimal calculateBillingPrice() throws PromoCodeExpiredException {
    return round(applyPromos(priceWithGear()));
  }
}
