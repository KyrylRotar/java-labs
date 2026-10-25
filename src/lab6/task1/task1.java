package lab6.task1;

import lab6.task1.model.Device;

import java.time.LocalDate;

public class task1 {
    public static void main(String[] args) {
        Device validDevice = new Device(
                "SN-ABC-123456",
                8.5,
                "HIGH_POWER_ZONE",
                true,
                LocalDate.of(2024, 1, 15)
        );

        Device invalidDevice = new Device(
                "BAD-ID",
                15.0,
                "STANDARD_ZONE",
                false,
                LocalDate.of(2023, 5, 20)
        );

        System.out.println("Valid device:");
        System.out.println(validDevice);

        System.out.println();
        System.out.println("Invalid device:");
        System.out.println(invalidDevice);
    }
}
