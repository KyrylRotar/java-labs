package lab5.task1;

import java.util.List;
import java.util.Optional;

public final class Main {
    private static final String TARGET_SERIAL_NUMBER = "SN-99887766";

    private Main() {
    }

    public static void main(String[] args) {
        StorageManager<Device> devices = new StorageManager<>();
        devices.add(new Device(TARGET_SERIAL_NUMBER, 1200, true, "Zone-B", 8, "Bosch"));
        devices.add(new Device("SN-11223344", 1800, true, "Zone-A", 3, "Makita"));
        devices.add(new Device("SN-55667788", 900, false, "Zone-B", 2, "Bosch"));
        devices.add(new Device("SN-OLD-0001", 700, false, "Zone-C", 0, "DeWalt"));

        Optional<Device> found = devices.findFirst(
                device -> TARGET_SERIAL_NUMBER.equals(device.getSerialNumber()));
        System.out.println("Пошук за серійним номером " + TARGET_SERIAL_NUMBER + ":");
        System.out.println(found.map(Device::toString).orElse("Пристрій не знайдено"));

        printMatches("Потужність не більше 1500 Вт",
                devices.filter(device -> device.getPowerWatts() <= 1500));
        printMatches("Є в наявності у зоні Zone-B",
                devices.filter(device -> device.isInStock()
                        && "Zone-B".equals(device.getWarehouseZone())));
        printMatches("Гарантія від 6 місяців або бренд Bosch",
                devices.filter(device -> device.getWarrantyMonthsLeft() >= 6
                        || "Bosch".equals(device.getBrand())));

        boolean removed = devices.removeFirst(
                device -> "SN-OLD-0001".equals(device.getSerialNumber()));
        System.out.println("Видалення за серійним номером SN-OLD-0001: "
                + (removed ? "виконано" : "не знайдено"));
        System.out.println("Кількість пристроїв після видалення: " + devices.size());
    }

    private static void printMatches(String title, List<Device> matches) {
        System.out.println("\n" + title + ":");
        if (matches.isEmpty()) {
            System.out.println("Збігів немає");
            return;
        }
        for (Device device : matches) {
            System.out.println(device);
        }
    }
}
