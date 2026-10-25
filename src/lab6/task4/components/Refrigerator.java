package lab6.task4.components;

public class Refrigerator {
    // Температура холодильної камери; клас навмисно не позначений @Component.
    private double temperature;

    /** Конструктор без параметрів відповідає спільній структурі прикладів компонентів. */
    public Refrigerator() {
    }

    /** Імітує відображення температури холодильника. */
    public void execute() {
        System.out.println("[Refrigerator] температура камери: " + temperature + "°C");
    }

    /** Повертає стан холодильника. */
    @Override
    public String toString() {
        return "Refrigerator{temperature=" + temperature + '}';
    }
}
