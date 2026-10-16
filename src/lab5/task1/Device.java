package lab5.task1;

import java.util.Objects;

public final class Device {
    private final String serialNumber;
    private final int powerWatts;
    private final boolean inStock;
    private final String warehouseZone;
    private final int warrantyMonthsLeft;
    private final String brand;

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

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public int getPowerWatts() {
        return powerWatts;
    }

    public boolean isInStock() {
        return inStock;
    }

    public String getWarehouseZone() {
        return warehouseZone;
    }

    public int getWarrantyMonthsLeft() {
        return warrantyMonthsLeft;
    }

    public String getBrand() {
        return brand;
    }

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
