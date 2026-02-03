package edu.eci.arsw.blacklistvalidator;

public class PerformanceMain {

    public static void main(String[] args) {

        HostBlackListsValidator validator = new HostBlackListsValidator();
        String ip = "202.24.34.55";

        int cores = Runtime.getRuntime().availableProcessors();

        int[] threadConfigurations = {
                1,
                cores,
                cores * 2,
                50,
                100,
                1000
        };

        System.out.println("Available cores: " + cores);
        System.out.println("--------------------------------");

        for (int threads : threadConfigurations) {

            long startTime = System.currentTimeMillis();

            validator.checkHost(ip, threads);

            long endTime = System.currentTimeMillis();

            System.out.println(
                    "Threads: " + threads +
                            " | Execution time: " + (endTime - startTime) + " ms"
            );
            //Pausa para diferenciar rendimientos en VisualVM
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
