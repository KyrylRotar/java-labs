package lab6.task4.components;

public class Refrigerator {
    private double temperature;

    public Refrigerator() {
    }

    public void execute() {
        System.out.println("[Refrigerator] температура камери: " + temperature + "°C");
    }

    @Override
    public String toString() {
        return "Refrigerator{temperature=" + temperature + '}';
    }
}
