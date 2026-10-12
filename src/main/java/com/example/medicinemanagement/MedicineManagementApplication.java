package com.example.medicinemanagement;

import com.example.medicinemanagement.tcp.TcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedicineManagementApplication {

    public static void main(String[] args) {

        var context = SpringApplication.run(
                MedicineManagementApplication.class,
                args
        );

        TcpServer tcpServer = context.getBean(TcpServer.class);

        Thread thread = new Thread(tcpServer::start);
        thread.setDaemon(true);
        thread.start();
    }
}