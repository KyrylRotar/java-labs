package lab5.task4;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Виконує п'ять запитів Stream API для завдання 4, варіант 13. */
public final class Main {
    private static final int PAGE_SKIP = 1;
    private static final int PAGE_SIZE = 7;

    private Main() {
    }

    public static void main(String[] args) {
        List<Device> devices = createDevices();

        // 1. Пристрої є в наявності, а їхня потужність не перевищує 1000 Вт.
        List<Device> availableLowPowerDevices = devices.stream()
                .filter(Device::isInStock)
                .filter(device -> device.getPowerWatts() <= 1000)
                .toList();
        printDevices("В наявності, потужність до 1000 Вт", availableLowPowerDevices);

        // 2. Сортування за категорією зростанням, потужністю спаданням; вибір моделі.
        List<String> modelsSortedByCategoryAndPower = devices.stream()
                .sorted(Comparator.comparing(Device::getCategory)
                        .thenComparing(Comparator.comparingInt(Device::getPowerWatts).reversed()))
                .map(Device::getModel)
                .toList();
        System.out.println("\nМоделі за категорією та потужністю:");
        modelsSortedByCategoryAndPower.forEach(System.out::println);

        // 3. Пагінація: пропускаємо перший пристрій і беремо наступні сім.
        List<Device> page = devices.stream()
                .skip(PAGE_SKIP)
                .limit(PAGE_SIZE)
                .toList();
        printDevices("Сторінка: пропущено 1, вибрано до 7 пристроїв", page);

        // 4. Перший пристрій Bosch із гарантією понад 24 місяці.
        Optional<Device> firstLongWarrantyBosch = devices.stream()
                .filter(device -> "Bosch".equals(device.getBrand()))
                .filter(device -> device.getWarrantyMonths() > 24)
                .findFirst();
        System.out.println("\nПерший Bosch із гарантією понад 24 місяці:");
        System.out.println(firstLongWarrantyBosch
                .map(Device::toString)
                .orElse("Відповідний пристрій не знайдено."));

        // 5. Пристрій із найбільшим гарантійним терміном.
        Optional<Device> longestWarrantyDevice = devices.stream()
                .max(Comparator.comparingInt(Device::getWarrantyMonths));
        System.out.println("\nПристрій із найдовшою гарантією:");
        System.out.println(longestWarrantyDevice
                .map(Device::toString)
                .orElse("Список пристроїв порожній."));
    }

    /** Створює тестову колекцію, що покриває всі умови варіанта. */
    private static List<Device> createDevices() {
        return List.of(
                new Device("Drill Pro 18V", "Інструмент", 850, true, "Bosch", 36),
                new Device("Angle Grinder 900", "Інструмент", 900, true, "Makita", 24),
                new Device("Impact Driver X", "Інструмент", 700, false, "DeWalt", 18),
                new Device("Cordless Drill 12V", "Інструмент", 500, true, "Bosch", 30),
                new Device("Air Purifier A2", "Побутова техніка", 250, true, "Philips", 24),
                new Device("Vacuum Cleaner V8", "Побутова техніка", 950, true, "Bosch", 48),
                new Device("Coffee Maker CM1", "Побутова техніка", 1100, true, "Philips", 24),
                new Device("Smart Sensor S1", "Сенсор", 15, true, "Xiaomi", 12),
                new Device("Smart Sensor S2", "Сенсор", 10, false, "Xiaomi", 12),
                new Device("Network Router R5", "Мережеве обладнання", 30, true, "TP-Link", 36),
                new Device("Switch 8-Port", "Мережеве обладнання", 45, true, "Bosch", 60)
        );
    }

    /** Виводить назву запиту та пристрої, або повідомлення про порожній результат. */
    private static void printDevices(String title, List<Device> result) {
        System.out.println("\n" + title + ":");
        if (result.isEmpty()) {
            System.out.println("Пристроїв не знайдено.");
            return;
        }
        result.forEach(System.out::println);
    }
}
