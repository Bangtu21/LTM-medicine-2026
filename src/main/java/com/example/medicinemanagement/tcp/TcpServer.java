package com.example.medicinemanagement.tcp;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@AllArgsConstructor
public class TcpServer {
    private final ClientHandler clientHandler;

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);

            System.out.println("=================================");
            System.out.println("TCP Server đang chạy");
            System.out.println("Port: 9999");
            System.out.println("=================================");

            while (true) {
                Socket socket = serverSocket.accept();

                System.out.println("Có client kết nối");

                clientHandler.handle(socket);
            }

        } catch (IOException e) {
            System.out.println("Lỗi TCP Server: " + e.getMessage());
        }
    }
}
