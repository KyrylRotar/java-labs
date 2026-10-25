package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "water-sensor", version = "0.9")
public class WaterSensor {
    private double humidity;

    public WaterSensor() {
    }

    public void execute() {
        System.out.println("[WaterSensor] вологість: " + humidity + "%");
    }

    @Override
    public String toString() {
        return "WaterSensor{humidity=" + humidity + '}';
    }
}
