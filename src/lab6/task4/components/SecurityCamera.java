package lab6.task4.components;

import lab6.task4.annotations.Component;

@Component(name = "camera", version = "3.1")
public class SecurityCamera {
    private String streamUrl;

    public SecurityCamera() {
    }

    public void execute() {
        System.out.println("[SecurityCamera] трансляція: " + streamUrl);
    }

    @Override
    public String toString() {
        return "SecurityCamera{streamUrl='" + streamUrl + "'}";
    }
}
