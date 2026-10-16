package lab5.task5;

/** Результат внутрішнього з'єднання пристрою зі складом. */
public final class DeviceWarehouseDto {
    private final String model;
    private final int powerWatts;
    private final String warehouseName;

    public DeviceWarehouseDto(String model, int powerWatts, String warehouseName) {
        this.model = model;
        this.powerWatts = powerWatts;
        this.warehouseName = warehouseName;
    }

    @Override
    public String toString() {
        return "DeviceWarehouseDto{" +
                "model='" + model + '\'' +
                ", powerWatts=" + powerWatts +
                ", warehouseName='" + warehouseName + '\'' +
                '}';
    }
}
