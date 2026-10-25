package lab6.task4.components;

public class WifiRouter {
    // Назва Wi-Fi мережі; клас навмисно не маркований @Component.
    private String networkName;

    /** Конструктор без параметрів відповідає загальному шаблону класів завдання. */
    public WifiRouter() {
    }

    /** Імітує виведення назви активної мережі. */
    public void execute() {
        System.out.println("[WifiRouter] активна мережа: " + networkName);
    }

    /** Повертає стан маршрутизатора. */
    @Override
    public String toString() {
        return "WifiRouter{networkName='" + networkName + "'}";
    }
}
