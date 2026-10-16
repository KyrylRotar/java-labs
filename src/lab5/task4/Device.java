package lab5.task4;

import java.util.Objects;

/** Модель пристрою для запитів Stream API у завданні 4. */
public final class Device {
    private final String model;
    private final String category;
    private final int powerWatts;
    private final boolean inStock;
    private final String brand;
    private final int warrantyMonths;

    public Device(String model, String category, int powerWatts,
                  boolean inStock, String brand, int warrantyMonths) {
        this.model = requireText(model, "model");
        this.category = requireText(category, "category");
        this.brand = requireText(brand, "brand");
        if (powerWatts < 0 || warrantyMonths < 0) {
            throw new IllegalArgumentException(
                    "Потужність і термін гарантії не можуть бути від'ємними.");
        }
        this.powerWatts = powerWatts;
        this.inStock = inStock;
        this.warrantyMonths = warrantyMonths;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " не може бути null.");
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " не може бути порожнім.");
        }
        return value;
    }

    public String getModel() {
        return model;
    }

    public String getCategory() {
        return category;
    }

    public int getPowerWatts() {
        return powerWatts;
    }

    public boolean isInStock() {
        return inStock;
    }

    public String getBrand() {
        return brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public String toString() {
        return "Device{" +
                "model='" + model + '\'' +
                ", category='" + category + '\'' +
                ", powerWatts=" + powerWatts +
                ", inStock=" + inStock +
                ", brand='" + brand + '\'' +
                ", warrantyMonths=" + warrantyMonths +
                '}';
    }
}
