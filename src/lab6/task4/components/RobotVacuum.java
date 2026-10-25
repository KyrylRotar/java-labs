package lab6.task4.components;

public class RobotVacuum {
    private int batteryLevel;

    public RobotVacuum() {
    }

    public void execute() {
        System.out.println("[RobotVacuum] заряд батареї: " + batteryLevel + "%");
    }

    @Override
    public String toString() {
        return "RobotVacuum{batteryLevel=" + batteryLevel + '}';
    }
}
