package lab6.task2;

import lab6.task1.model.Device;

import java.time.LocalDate;

/** Демонструє Завдання 2: перевірку валідного та невалідного Device. */
public final class main {
    private main() {
    }

    public static void main(String[] args) {
        Device validDevice = new Device(
                "SN-ABC-123456", 25.0, "HIGH_POWER_ZONE", true, LocalDate.of(2023, 5, 14));
        Device invalidDevice = new Device(
                "BAD-1", 750.0, "GENERAL_ZONE", false, LocalDate.of(2022, 1, 10));

        EntityValidator validator = new ValidatorService();
        printResult("Валідний об'єкт", validDevice, validator.validate(validDevice));
        printResult("Невалідний об'єкт", invalidDevice, validator.validate(invalidDevice));
    }

    private static void printResult(String title, Device device, ValidationResult result) {
        System.out.println("=== " + title + " ===");
        System.out.println(device);
        System.out.println("IsValid: " + result.isValid());
        if (!result.isValid()) {
            System.out.println("Помилки (" + result.getErrors().size() + "):");
            for (ValidationError error : result.getErrors()) {
                System.out.println("  - [" + error.fieldName() + "] " + error.message());
            }
        }
        System.out.println();
    }
}
