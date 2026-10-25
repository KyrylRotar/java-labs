package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "lamp", version = "2.0")
public class SmartLamp {
    // Рівень яскравості у відсотках, задається рушієм через Reflection.
    private int brightness;

    /** Конструктор без параметрів потрібен для створення через Reflection. */
    public SmartLamp() {
    }

    /** Демонструє дію лампи з установленим рівнем яскравості. */
    public void execute() {
        System.out.println("[SmartLamp] яскравість встановлено на " + brightness + "%");
    }

    /** Показує поточне значення яскравості. */
    @Override
    public String toString() {
        return "SmartLamp{brightness=" + brightness + '}';
    }
}
