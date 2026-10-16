package lab5.task1;

import java.util.Objects;

/** Описує пристрій зі складськими та технічними характеристиками. */
public final class Device {
    /** Серійний номер пристрою. */
    private final String serialNumber;
    /** Потужність пристрою у ватах. */
    private final int powerWatts;
    /** Чи є пристрій у наявності. */
    private final boolean inStock;
    /** Зона складу, де зберігається пристрій. */
    private final String warehouseZone;
    /** Кількість місяців гарантії, що залишилася. */
    private final int warrantyMonthsLeft;
    /** Назва бренду пристрою. */
    private final String brand;

    /** Створює пристрій і перевіряє коректність його характеристик. */
    public Device(String serialNumber, int powerWatts, boolean inStock,
                  String warehouseZone, int warrantyMonthsLeft, String brand) {
        this.serialNumber = requireText(serialNumber, "serialNumber");
        this.warehouseZone = requireText(warehouseZone, "warehouseZone");
        this.brand = requireText(brand, "brand");
        if (powerWatts < 0) {
            throw new IllegalArgumentException("powerWatts must not be negative");
        }
        if (warrantyMonthsLeft < 0) {
            throw new IllegalArgumentException("warrantyMonthsLeft must not be negative");
        }
        this.powerWatts = powerWatts;
        this.inStock = inStock;
        this.warrantyMonthsLeft = warrantyMonthsLeft;
    }

    /** Перевіряє, що текстове значення задане і не є порожнім. */
    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    /** Повертає серійний номер. */
    public String getSerialNumber() {
        return serialNumber;
    }

    /** Повертає потужність у ватах. */
    public int getPowerWatts() {
        return powerWatts;
    }

    /** Повертає true, якщо пристрій є в наявності. */
    public boolean isInStock() {
        return inStock;
    }

    /** Повертає зону складу. */
    public String getWarehouseZone() {
        return warehouseZone;
    }

    /** Повертає кількість місяців гарантії, що залишилася. */
    public int getWarrantyMonthsLeft() {
        return warrantyMonthsLeft;
    }

    /** Повертає назву бренду. */
    public String getBrand() {
        return brand;
    }

    /** Формує текстове представлення всіх характеристик пристрою. */
    @Override
    public String toString() {
        return "Device{" +
                "serialNumber='" + serialNumber + '\'' +
                ", powerWatts=" + powerWatts +
                ", inStock=" + inStock +
                ", warehouseZone='" + warehouseZone + '\'' +
                ", warrantyMonthsLeft=" + warrantyMonthsLeft +
                ", brand='" + brand + '\'' +
                '}';
    }
}
