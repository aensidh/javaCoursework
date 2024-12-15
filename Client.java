import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Client {
    private String host;
    private int port;
    private int packetCount;
    private int packetSize;

    public Client(String host, int port, int packetCount, int packetSize) {
        this.host = host;
        this.port = port;
        this.packetCount = packetCount;
        this.packetSize = packetSize;
    }

    public void startClient() {
        try (Socket socket = new Socket(host, port)) {
            System.out.println("Подключено к серверу: " + host + ":" + port);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());

            long totalLatency = 0;
            int successfulPackets = 0;

            for (int i = 1; i <= packetCount; i++) {
                byte[] packet = generatePacket(packetSize);
                long startTime = System.nanoTime();

                out.write(packet);
                out.flush();

                byte[] response = new byte[packetSize];
                int bytesRead = in.read(response);
                long endTime = System.nanoTime();

                if (bytesRead > 0 && Arrays.equals(packet, Arrays.copyOf(response, bytesRead))) {
                    long latencyNs = (endTime - startTime) / 2;
                    double latencyMs = latencyNs / 1_000_000.0;
                    totalLatency += latencyNs;
                    successfulPackets++;
                    System.out.printf("Пакет %d (%d байт) доставлен. Время задержки: %d нс (%.3f мс).%n",
                            i, bytesRead, latencyNs, latencyMs);
                } else {
                    System.out.println("Пакет " + i + " не был доставлен.");
                }

                Thread.sleep(100);
            }

            if (successfulPackets > 0) {
                System.out.println("Успешно доставлено пакетов: " + successfulPackets);
                System.out.printf("Средняя задержка: %d нс (%.3f мс).%n",
                        totalLatency / successfulPackets,
                        totalLatency / successfulPackets / 1_000_000.0);
            } else {
                System.out.println("Не удалось доставить ни одного пакета.");
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        }
    }

    private byte[] generatePacket(int size) {
        byte[] packet = new byte[size];
        Arrays.fill(packet, (byte) 1);
        return packet;
    }
}
