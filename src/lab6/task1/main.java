package lab6.task1;

import lab6.task1.model.Device;

import java.time.LocalDate;

/** Демонструє створення Device з коректними та некоректними даними для анотацій. */
public class main {
    public static void main(String[] args) {
        // Коректний приклад: серійний номер відповідає шаблону,
        // потужність у межах 0.1–500 кВт, дата задана як LocalDate.
        Device validDevice = new Device(
                "SN-ABC-123456",
                8.5,
                "HIGH_POWER_ZONE",
                true,
                LocalDate.of(2024, 1, 15)
        );

        // Некоректний приклад: серійний номер не відповідає шаблону.
        // Потужність перевищує поріг 10 кВт, але зона не є HIGH_POWER_ZONE,
        // тому також порушується комплексне правило рівня класу.
        Device invalidDevice = new Device(
                "BAD-ID",
                15.0,
                "STANDARD_ZONE",
                false,
                LocalDate.of(2023, 5, 20)
        );

        // Завдання 1 лише описує метадані анотаціями; перевірки виконує валідатор
        // із Завдання 2. Тут виводимо об'єкти, щоб побачити їхні початкові дані.
        System.out.println("Коректний приклад Device:");
        System.out.println(validDevice);

        System.out.println();
        System.out.println("Приклад Device з порушеннями:");
        System.out.println(invalidDevice);
    }
}
