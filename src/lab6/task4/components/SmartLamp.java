package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "lamp", version = "2.0")
public class SmartLamp {
    private int brightness;

    public SmartLamp() {
    }

    public void execute() {
        System.out.println("[SmartLamp] яскравість встановлено на " + brightness + "%");
    }

    @Override
    public String toString() {
        return "SmartLamp{brightness=" + brightness + '}';
    }
}
