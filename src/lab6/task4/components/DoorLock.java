package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "door-lock")
public class DoorLock {
    // Стан замка: true означає, що двері зачинені.
    private boolean locked;

    /** Конструктор без параметрів використовується Reflection-рушієм. */
    public DoorLock() {
    }

    /** Виводить дію замка залежно від поточного стану. */
    public void execute() {
        System.out.println("[DoorLock] двері " + (locked ? "зачинено" : "відчинено"));
    }

    /** Повертає стан замка після виконання дії. */
    @Override
    public String toString() {
        return "DoorLock{locked=" + locked + '}';
    }
}
