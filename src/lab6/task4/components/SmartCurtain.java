package lab6.task4.components;

public class SmartCurtain {
    // Стан штор: true означає, що вони відкриті; клас не має @Component.
    private boolean open;

    /** Конструктор без параметрів відповідає структурі інших компонентів. */
    public SmartCurtain() {
    }

    /** Імітує відкривання або закривання штор. */
    public void execute() {
        System.out.println("[SmartCurtain] штори " + (open ? "відкрито" : "закрито"));
    }

    /** Повертає поточний стан штор. */
    @Override
    public String toString() {
        return "SmartCurtain{open=" + open + '}';
    }
}
