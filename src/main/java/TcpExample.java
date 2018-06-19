import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        TcpServer server = new TcpServer();
        TcpClient client = new TcpClient();

        server.start();
        client.start();
    }

    private static class TcpServer extends Thread {
        @Override
        public void run() {
            // Создаем сокет на порту 12345.
            try (ServerSocket serverSocket = new ServerSocket(12345)) {
                // Слушаем входящие подключения на 12345.
                // Здесь поток блокируется, пока не будет выполнено
                // подключние.
                try (Socket socket = serverSocket.accept()) {
                    // Когда соединение установлено метод accept() возвращает
                    // сокет, который предоставляет потоки ввода/вывода.
                    // Таким образом, передача данных не отличается от
                    // чтения/записи файла.
                    InputStream in = socket.getInputStream();

                    // Читаем некоторые данные.
                    byte code = (byte) in.read();

                    System.out.println(code);

                    // Когда коммуникация окончена - необходимо закрыть сокет,
                    // клиент получит соответствующее сообщение и так же закроет
                    // соединение.
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class TcpClient extends Thread {
        @Override
        public void run() {
            // Создаем сокет.
            try (Socket socket = new Socket()) {
                // Устанавливаем подключение к серверу, для этого нужно знать адрес и порт.
                socket.connect(new InetSocketAddress("localhost", 12345));

                // так же как  с сервером из сокета получаем потоки ввода/вывода.
                OutputStream out = socket.getOutputStream();

                // Отправляем некоторые данные.
                out.write(10);

                // Для закрытия подключения так же достаточно закрыть соке.
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
