package lab6.task4.components;

public class WifiRouter {
    private String networkName;

    public WifiRouter() {
    }

    public void execute() {
        System.out.println("[WifiRouter] активна мережа: " + networkName);
    }

    @Override
    public String toString() {
        return "WifiRouter{networkName='" + networkName + "'}";
    }
}
