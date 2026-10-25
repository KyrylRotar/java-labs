package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "thermostat", version = "1.2")
public class Thermostat {
    // Цільова температура, яку ComponentEngine встановлює через Reflection.
    private double targetTemperature;

    /** Публічний конструктор без параметрів використовується для динамічного створення. */
    public Thermostat() {
    }

    /** Імітує встановлення температури термостата. */
    public void execute() {
        System.out.println("[Thermostat] встановлено температуру " + targetTemperature + "°C");
    }

    /** Повертає поточний стан компонента для виведення після виконання. */
    @Override
    public String toString() {
        return "Thermostat{targetTemperature=" + targetTemperature + '}';
    }
}
