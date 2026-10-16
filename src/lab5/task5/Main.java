package lab5.task5;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Виконує групування, агрегацію та з'єднання для завдання 5, варіант 13. */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        List<Warehouse> warehouses = createWarehouses();
        List<Device> devices = createDevices();

        // 1. Групуємо пристрої за складом і рахуємо їхню кількість.
        Map<String, Long> deviceCountsByWarehouse = devices.stream()
                .collect(Collectors.groupingBy(
                        Device::getWarehouseId,
                        Collectors.counting()));
        System.out.println("Кількість пристроїв на кожному складі:");
        warehouses.forEach(warehouse -> System.out.println(
                warehouse.getName() + ": "
                        + deviceCountsByWarehouse.getOrDefault(warehouse.getId(), 0L)));

        // 2. Обчислюємо середню потужність пристроїв для кожного складу.
        Map<String, Double> averagePowerByWarehouse = devices.stream()
                .collect(Collectors.groupingBy(
                        Device::getWarehouseId,
                        Collectors.averagingInt(Device::getPowerWatts)));
        System.out.println("\nСередня потужність пристроїв на кожному складі:");
        warehouses.forEach(warehouse -> {
            Double average = averagePowerByWarehouse.get(warehouse.getId());
            System.out.println(warehouse.getName() + ": "
                    + (average == null ? "немає пристроїв" : average + " Вт"));
        });

        // 3. Виконуємо внутрішнє з'єднання пристроїв зі складами за warehouseId.
        List<DeviceWarehouseDto> joinedDevices = devices.stream()
                .flatMap(device -> warehouses.stream()
                        .filter(warehouse -> warehouse.getId().equals(device.getWarehouseId()))
                        .map(warehouse -> new DeviceWarehouseDto(
                                device.getModel(),
                                device.getPowerWatts(),
                                warehouse.getName())))
                .toList();
        System.out.println("\nПристрої зі складом (Inner Join):");
        joinedDevices.forEach(System.out::println);

        // 4. Для кожного складу зберігаємо список пристроїв, включно з порожнім списком.
        List<WarehouseDevicesDto> devicesByWarehouse = warehouses.stream()
                .map(warehouse -> new WarehouseDevicesDto(
                        warehouse.getName(),
                        devices.stream()
                                .filter(device -> warehouse.getId().equals(device.getWarehouseId()))
                                .toList()))
                .toList();
        System.out.println("\nСклади та їхні пристрої (GroupJoin / Left Join):");
        devicesByWarehouse.forEach(System.out::println);

        // 5. Об'єднуємо назви всіх пристроїв у наявності через символ табуляції.
        String availableDeviceModels = devices.stream()
                .filter(Device::isInStock)
                .map(Device::getModel)
                .collect(Collectors.joining("\t"));
        System.out.println("\nНазви пристроїв у наявності через табуляцію:");
        System.out.println(availableDeviceModels);
    }

    /** Створює склади, зокрема склад без пристроїв для демонстрації Left Join. */
    private static List<Warehouse> createWarehouses() {
        return List.of(
                new Warehouse("WH-01", "Центральний склад"),
                new Warehouse("WH-02", "Північний склад"),
                new Warehouse("WH-03", "Південний склад")
        );
    }

    /** Створює пристрої з посиланнями на склади за їхніми ідентифікаторами. */
    private static List<Device> createDevices() {
        return List.of(
                new Device("Drill Pro 18V", "WH-01", 850, true),
                new Device("Angle Grinder 900", "WH-01", 900, true),
                new Device("Impact Driver X", "WH-01", 700, false),
                new Device("Vacuum Cleaner V8", "WH-02", 950, true),
                new Device("Coffee Maker CM1", "WH-02", 1100, false),
                new Device("Smart Sensor S1", "WH-02", 15, true)
        );
    }
}
