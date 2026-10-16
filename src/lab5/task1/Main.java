package lab5.task1;

import java.util.List;
import java.util.Optional;

/** Демонструє роботу StorageManager на прикладі пристроїв (варіант 13). */
public final class Main {
    /** Серійний номер, за яким виконується пошук пристрою з умови. */
    private static final String TARGET_SERIAL_NUMBER = "SN-99887766";

    /** Забороняє створення екземпляра класу з точкою входу. */
    private Main() {
    }

    /** Створює тестові пристрої та демонструє пошук, фільтрацію і видалення. */
    public static void main(String[] args) {
        // Створюємо сховище пристроїв і додаємо до нього тестові дані.
        StorageManager<Device> devices = new StorageManager<>();
        devices.add(new Device(TARGET_SERIAL_NUMBER, 1200, true, "Zone-B", 8, "Bosch"));
        devices.add(new Device("SN-11223344", 1800, true, "Zone-A", 3, "Makita"));
        devices.add(new Device("SN-55667788", 900, false, "Zone-B", 2, "Bosch"));
        devices.add(new Device("SN-OLD-0001", 700, false, "Zone-C", 0, "DeWalt"));

        // Шукаємо пристрій за точним серійним номером.
        Optional<Device> found = devices.findFirst(
                device -> TARGET_SERIAL_NUMBER.equals(device.getSerialNumber()));
        System.out.println("Пошук за серійним номером " + TARGET_SERIAL_NUMBER + ":");
        System.out.println(found.map(Device::toString).orElse("Пристрій не знайдено"));

        // Предикат 1 варіанта: потужність не перевищує 1500 Вт.
        printMatches("Потужність не більше 1500 Вт",
                devices.filter(device -> device.getPowerWatts() <= 1500));
        // Предикат 2 варіанта: пристрій є у наявності та лежить у зоні Zone-B.
        printMatches("Є в наявності у зоні Zone-B",
                devices.filter(device -> device.isInStock()
                        && "Zone-B".equals(device.getWarehouseZone())));
        // Предикат 3 варіанта: гарантія від 6 місяців або бренд Bosch.
        printMatches("Гарантія від 6 місяців або бренд Bosch",
                devices.filter(device -> device.getWarrantyMonthsLeft() >= 6
                        || "Bosch".equals(device.getBrand())));

        // Видаляємо пристрій за серійним номером і показуємо розмір сховища.
        boolean removed = devices.removeFirst(
                device -> "SN-OLD-0001".equals(device.getSerialNumber()));
        System.out.println("Видалення за серійним номером SN-OLD-0001: "
                + (removed ? "виконано" : "не знайдено"));
        System.out.println("Кількість пристроїв після видалення: " + devices.size());
    }

    /** Виводить заголовок і список знайдених пристроїв або повідомляє про відсутність збігів. */
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
