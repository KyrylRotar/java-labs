package lab6.task1.model;

import lab6.task1.annotations.HighPowerZoneRule;
import lab6.task1.annotations.InRange;
import lab6.task1.annotations.MatchesRegex;

import java.time.LocalDate;

@HighPowerZoneRule(
        powerField = "powerKw",
        zoneField = "storageZone",
        powerThreshold = 10.0,
        requiredZone = "HIGH_POWER_ZONE",
        message = "Пристрої потужністю понад 10 кВт повинні зберігатися у HIGH_POWER_ZONE"
)
public class Device {

    @MatchesRegex(
            pattern = "^SN-[A-Z]{3}-\\d{6}$",
            message = "Серійний номер має формат SN-XXX-000000"
    )
    private String serialNumber; // String

    @InRange(
            min = 0.1,
            max = 500.0,
            message = "Потужність має бути в межах 0.1–500 кВт"
    )
    private double powerKw; // double

    private String storageZone; // String (зона складу)

    private boolean operational; // boolean

    private LocalDate manufactureDate; // LocalDate

    public Device() {
    }

    public Device(String serialNumber, double powerKw, String storageZone,
                  boolean operational, LocalDate manufactureDate) {
        this.serialNumber = serialNumber;
        this.powerKw = powerKw;
        this.storageZone = storageZone;
        this.operational = operational;
        this.manufactureDate = manufactureDate;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getPowerKw() {
        return powerKw;
    }

    public void setPowerKw(double powerKw) {
        this.powerKw = powerKw;
    }

    public String getStorageZone() {
        return storageZone;
    }

    public void setStorageZone(String storageZone) {
        this.storageZone = storageZone;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }

    public LocalDate getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(LocalDate manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    @Override
    public String toString() {
        return "Device{serialNumber='%s', powerKw=%.1f, storageZone='%s', operational=%b, manufactureDate=%s}"
                .formatted(serialNumber, powerKw, storageZone, operational, manufactureDate);
    }
}
