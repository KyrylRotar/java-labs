package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "camera", version = "3.1")
public class SecurityCamera {
    // Адреса трансляції, яку динамічно задає ComponentEngine.
    private String streamUrl;

    /** Конструктор без параметрів необхідний для створення компонента через Reflection. */
    public SecurityCamera() {
    }

    /** Імітує запуск трансляції з установленої адреси. */
    public void execute() {
        System.out.println("[SecurityCamera] трансляція: " + streamUrl);
    }

    /** Виводить адресу потоку камери. */
    @Override
    public String toString() {
        return "SecurityCamera{streamUrl='" + streamUrl + "'}";
    }
}
