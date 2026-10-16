package lab5.task5;

import java.util.Objects;

/** Пристрій, пов'язаний зі складом за ідентифікатором складу. */
public final class Device {
    private final String model;
    private final String warehouseId;
    private final int powerWatts;
    private final boolean inStock;

    public Device(String model, String warehouseId, int powerWatts, boolean inStock) {
        this.model = requireText(model, "model");
        this.warehouseId = requireText(warehouseId, "warehouseId");
        if (powerWatts < 0) {
            throw new IllegalArgumentException("Потужність не може бути від'ємною.");
        }
        this.powerWatts = powerWatts;
        this.inStock = inStock;
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

    public String getWarehouseId() {
        return warehouseId;
    }

    public int getPowerWatts() {
        return powerWatts;
    }

    public boolean isInStock() {
        return inStock;
    }

    @Override
    public String toString() {
        return "Device{" +
                "model='" + model + '\'' +
                ", warehouseId='" + warehouseId + '\'' +
                ", powerWatts=" + powerWatts +
                ", inStock=" + inStock +
                '}';
    }
}
