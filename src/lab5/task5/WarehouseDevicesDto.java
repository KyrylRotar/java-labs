package lab5.task5;

import java.util.List;

/** Склад та список усіх пристроїв, пов'язаних із ним. */
public final class WarehouseDevicesDto {
    private final String warehouseName;
    private final List<Device> devices;

    public WarehouseDevicesDto(String warehouseName, List<Device> devices) {
        this.warehouseName = warehouseName;
        this.devices = List.copyOf(devices);
    }

    @Override
    public String toString() {
        return "WarehouseDevicesDto{" +
                "warehouseName='" + warehouseName + '\'' +
                ", devices=" + devices +
                '}';
    }
}
