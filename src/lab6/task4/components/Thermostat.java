package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "thermostat", version = "1.2")
public class Thermostat {
    private double targetTemperature;

    public Thermostat() {
    }

    public void execute() {
        System.out.println("[Thermostat] встановлено температуру " + targetTemperature + "°C");
    }

    @Override
    public String toString() {
        return "Thermostat{targetTemperature=" + targetTemperature + '}';
    }
}
