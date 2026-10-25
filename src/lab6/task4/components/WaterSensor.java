package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "water-sensor", version = "0.9")
public class WaterSensor {
    // Поточне значення вологості, встановлюється тестовим Reflection-рушієм.
    private double humidity;

    /** Конструктор без параметрів дозволяє створити сенсор через Reflection. */
    public WaterSensor() {
    }

    /** Імітує зчитування даних сенсора вологості. */
    public void execute() {
        System.out.println("[WaterSensor] вологість: " + humidity + "%");
    }

    /** Повертає стан сенсора для підсумкового виводу. */
    @Override
    public String toString() {
        return "WaterSensor{humidity=" + humidity + '}';
    }
}
