package com.example.medicinemanagement.tcp;

import com.example.medicinemanagement.config.TcpConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Component
@AllArgsConstructor
public class TcpClient {
    private final TcpConfig tcpConfig;

    public String send(String message) {

        try {
            Socket socket = new Socket(
                    tcpConfig.getHost(),
                    tcpConfig.getPort()
            );

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            writer.println(message);

            String response = reader.readLine();

            socket.close();

            return response;

        } catch (IOException e) {
            return "Lỗi kết nối TCP Server: " + e.getMessage();
        }
    }
}