package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "speaker", version = "1.5")
public class SmartSpeaker {
    // Гучність динаміка у відсотках.
    private int volume;

    /** Конструктор без параметрів потрібен для динамічної інстанціації. */
    public SmartSpeaker() {
    }

    /** Демонструє застосування налаштованої гучності. */
    public void execute() {
        System.out.println("[SmartSpeaker] гучність встановлено на " + volume + "%");
    }

    /** Повертає поточний рівень гучності. */
    @Override
    public String toString() {
        return "SmartSpeaker{volume=" + volume + '}';
    }
}
