package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "door-lock")
public class DoorLock {
    private boolean locked;

    public DoorLock() {
    }

    public void execute() {
        System.out.println("[DoorLock] двері " + (locked ? "зачинено" : "відчинено"));
    }

    @Override
    public String toString() {
        return "DoorLock{locked=" + locked + '}';
    }
}
