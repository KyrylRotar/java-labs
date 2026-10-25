package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "speaker", version = "1.5")
public class SmartSpeaker {
    private int volume;

    public SmartSpeaker() {
    }

    public void execute() {
        System.out.println("[SmartSpeaker] гучність встановлено на " + volume + "%");
    }

    @Override
    public String toString() {
        return "SmartSpeaker{volume=" + volume + '}';
    }
}
