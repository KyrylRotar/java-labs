package lab6.task4.components;

public class SmartCurtain {
    private boolean open;

    public SmartCurtain() {
    }

    public void execute() {
        System.out.println("[SmartCurtain] штори " + (open ? "відкрито" : "закрито"));
    }

    @Override
    public String toString() {
        return "SmartCurtain{open=" + open + '}';
    }
}
