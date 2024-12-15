import java.io.*;
import java.net.*;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен на порту " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка работы сервера: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (InputStream in = socket.getInputStream();
                 OutputStream out = socket.getOutputStream()) {
                byte[] buffer = new byte[1500]; // Максимальный размер MTU
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead > 0) {
                        System.out.printf("Получен пакет размером %d байт: %s%n", bytesRead, new String(buffer, 0, bytesRead));
                        out.write(buffer, 0, bytesRead);
                        out.flush();
                        System.out.printf("Отправлен пакет размером %d байт.%n", bytesRead);
                    } else if (bytesRead == -1) {
                        System.out.println("Соединение закрыто клиентом.");
                        break;
                    } else {
                        System.out.println("Прочитано 0 байт, пустой пакет.");
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка обработки клиента: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Ошибка закрытия сокета: " + e.getMessage());
                }
            }
        }
    }
}
