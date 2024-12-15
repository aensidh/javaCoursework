import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Введите режим работы (server/client/exit): ");
            String mode = scanner.nextLine().trim().toLowerCase();

            switch (mode) {
                case "server":
                    int port = getValidPort(scanner);
                    Server server = new Server(port);
                    server.startServer();
                    break;
                case "client":
                    System.out.println("Введите хост сервера: ");
                    String host = scanner.next();
                    scanner.nextLine();

                    int clientPort = getValidPort(scanner);
                    System.out.println("Введите количество пакетов: ");
                    int packetCount = getValidIntegerInput(scanner);

                    int packetSize = getValidPacketSize(scanner);

                    Client client = new Client(host, clientPort, packetCount, packetSize);
                    client.startClient();
                    break;
                case "exit":
                    System.out.println("Выход из программы. До свидания!");
                    running = false;
                    break;
                default:
                    System.out.println("Неверный режим! Введите 'server', 'client' или 'exit'.");
            }
        }
        scanner.close();
    }

    private static int getValidPort(Scanner scanner) {
        int port = -1;
        while (port < 0 || port > 65535) {
            System.out.println("Введите порт (от 0 до 65535): ");
            port = getValidIntegerInput(scanner);
            if (port < 0 || port > 65535) {
                System.out.println("Некорректное значение порта! Попробуйте снова.");
            }
        }
        return port;
    }

    private static int getValidPacketSize(Scanner scanner) {
        int size = -1;
        while (size < 1 || size > 1500) {
            System.out.println("Введите размер пакета (1-1500 байт): ");
            size = getValidIntegerInput(scanner);
            if (size < 1 || size > 1500) {
                System.out.println("Некорректный размер пакета! Попробуйте снова.");
            }
        }
        return size;
    }

    private static int getValidIntegerInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка ввода! Введите числовое значение.");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine();
        return value;
    }
}
