package model;

import java.math.BigDecimal;

public final class ProtectiveGear {
  private final String name;
  private BigDecimal hourlyPrice;

  public ProtectiveGear(String name, BigDecimal hourlyPrice) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Название экипировки не должно быть пустым");
    }
    this.name = name.strip();
    setHourlyPrice(hourlyPrice);
  }

  public String getName() {
    return name;
  }

  public BigDecimal getHourlyPrice() {
    return hourlyPrice;
  }

  public void setHourlyPrice(BigDecimal hourlyPrice) {
    if (hourlyPrice == null || hourlyPrice.signum() < 0) {
      throw new IllegalArgumentException("Стоимость экипировки не может быть отрицательной");
    }
    this.hourlyPrice = hourlyPrice;
  }
}
