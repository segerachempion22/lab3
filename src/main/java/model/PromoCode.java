package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import exception.InvalidPromoCodeException;
import exception.PromoCodeExpiredException;

public final class PromoCode {
  private final String code;
  private final BigDecimal discountPercent;
  private final LocalDate expirationDate;

  public PromoCode(String code, BigDecimal discountPercent, LocalDate expirationDate) {
    if (code == null || !code.matches("[A-Za-z0-9_-]+") || discountPercent == null
        || discountPercent.signum() < 0 || discountPercent.compareTo(new BigDecimal("100")) > 0
        || expirationDate == null) {
      throw new InvalidPromoCodeException("Некорректный промокод, скидка или срок действия");
    }
    this.code = code;
    this.discountPercent = discountPercent;
    this.expirationDate = expirationDate;
  }

  public String getCode() {
    return code;
  }

  public BigDecimal getDiscountPercent() {
    return discountPercent;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public void validateOn(LocalDate date) throws PromoCodeExpiredException {
    if (date.isAfter(expirationDate)) {
      throw new PromoCodeExpiredException("Срок действия промокода " + code + " истек");
    }
  }
}
