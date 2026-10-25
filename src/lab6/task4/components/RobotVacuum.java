package lab6.task4.components;

public class RobotVacuum {
    // Рівень заряду батареї; клас використовується як приклад без @Component.
    private int batteryLevel;

    /** Конструктор без параметрів відповідає вимогам структури класу. */
    public RobotVacuum() {
    }

    /** Імітує перевірку заряду робота-пилососа. */
    public void execute() {
        System.out.println("[RobotVacuum] заряд батареї: " + batteryLevel + "%");
    }

    /** Повертає стан батареї. */
    @Override
    public String toString() {
        return "RobotVacuum{batteryLevel=" + batteryLevel + '}';
    }
}
