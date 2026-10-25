package lab6.task3;

import lab6.task1.model.Device;
import lab6.task2.EntityValidator;
import lab6.task2.ValidationResult;
import lab6.task2.ValidatorService;

import java.time.LocalDate;
import java.util.Random;

/** Порівнює базову та кешовану валідацію на однаковому наборі даних. */
public final class ValidationBenchmark {
    private static final int ITERATIONS = 500_000;
    private static final int WARMUP_ITERATIONS = 50_000;
    private static final int MEASURED_ROUNDS = 3;

    private ValidationBenchmark() {
    }

    public static void main(String[] args) {
        Device[] devices = generateDevices(ITERATIONS);
        EntityValidator regular = new ValidatorService();
        CachedValidationService cached = new CachedValidationService();

        // Прогріваємо JVM та кеш до замірювання.
        runCount(regular, devices, WARMUP_ITERATIONS);
        runCount(cached, devices, WARMUP_ITERATIONS);

        long regularBestNanos = Long.MAX_VALUE;
        long cachedBestNanos = Long.MAX_VALUE;
        long regularValidCount = -1;
        long cachedValidCount = -1;

        // Для обох реалізацій використовуються ті самі об'єкти та кількість проходів.
        for (int round = 0; round < MEASURED_ROUNDS; round++) {
            Measurement regularRun = measure(regular, devices);
            Measurement cachedRun = measure(cached, devices);

            if (regularRun.elapsedNanos() < regularBestNanos) {
                regularBestNanos = regularRun.elapsedNanos();
                regularValidCount = regularRun.validCount();
            }
            if (cachedRun.elapsedNanos() < cachedBestNanos) {
                cachedBestNanos = cachedRun.elapsedNanos();
                cachedValidCount = cachedRun.validCount();
            }
        }

        boolean resultsMatch = haveSameResults(regular, cached, devices);
        printResults(regularBestNanos, cachedBestNanos, regularValidCount,
                cachedValidCount, resultsMatch, cached.cachedTypeCount());
    }

    private static Device[] generateDevices(int count) {
        Random random = new Random(13);
        Device[] devices = new Device[count];
        for (int i = 0; i < count; i++) {
            String serialNumber = random.nextBoolean()
                    ? "SN-ABC-%06d".formatted(random.nextInt(1_000_000))
                    : "INVALID-%d".formatted(i);
            double powerKw = Math.round(random.nextDouble() * 600.0 * 10.0) / 10.0;
            String zone = random.nextBoolean() ? "HIGH_POWER_ZONE" : "GENERAL_ZONE";
            devices[i] = new Device(serialNumber, powerKw, zone,
                    random.nextBoolean(), LocalDate.of(2020, 1, 1).plusDays(random.nextInt(2_000)));
        }
        return devices;
    }

    private static Measurement measure(EntityValidator validator, Device[] devices) {
        long start = System.nanoTime();
        long validCount = runCount(validator, devices, devices.length);
        return new Measurement(System.nanoTime() - start, validCount);
    }

    private static long runCount(EntityValidator validator, Device[] devices, int count) {
        long validCount = 0;
        for (int i = 0; i < count; i++) {
            if (validator.validate(devices[i % devices.length]).isValid()) {
                validCount++;
            }
        }
        return validCount;
    }

    /** Порівнює повні результати валідації, а не лише кількість валідних записів. */
    private static boolean haveSameResults(EntityValidator first, EntityValidator second, Device[] devices) {
        for (Device device : devices) {
            ValidationResult firstResult = first.validate(device);
            ValidationResult secondResult = second.validate(device);
            if (firstResult.isValid() != secondResult.isValid()
                    || !firstResult.getErrors().equals(secondResult.getErrors())) {
                return false;
            }
        }
        return true;
    }

    private static void printResults(long regularNanos, long cachedNanos,
                                     long regularValid, long cachedValid,
                                     boolean resultsMatch, int cachedTypeCount) {
        double regularMillis = regularNanos / 1_000_000.0;
        double cachedMillis = cachedNanos / 1_000_000.0;
        double regularNanosPerCall = (double) regularNanos / ITERATIONS;
        double cachedNanosPerCall = (double) cachedNanos / ITERATIONS;
        double speedup = cachedMillis == 0.0 ? 0.0 : regularMillis / cachedMillis;

        System.out.println("+-------------------------------+------------+-------------+--------------+");
        System.out.println("| Сервіс                        | Час, мс    | нс/виклик   | Валідних     |");
        System.out.println("+-------------------------------+------------+-------------+--------------+");
        System.out.printf("| %-29s | %10.2f | %11.1f | %12d |%n",
                "ValidatorService (без кешу)", regularMillis, regularNanosPerCall, regularValid);
        System.out.printf("| %-29s | %10.2f | %11.1f | %12d |%n",
                "CachedValidationService", cachedMillis, cachedNanosPerCall, cachedValid);
        System.out.println("+-------------------------------+------------+-------------+--------------+");
        System.out.printf("Прискорення: x%.2f%n", speedup);
        System.out.println("Результати збігаються: " + resultsMatch);
        System.out.println("Типів у кеші: " + cachedTypeCount);
    }

    private record Measurement(long elapsedNanos, long validCount) {
    }
}
