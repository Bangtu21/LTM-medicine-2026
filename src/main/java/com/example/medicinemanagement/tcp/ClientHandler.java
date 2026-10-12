package com.example.medicinemanagement.tcp;

import com.example.medicinemanagement.entity.Medicine;
import com.example.medicinemanagement.entity.Usage;
import com.example.medicinemanagement.service.MedicineService;
import com.example.medicinemanagement.service.UsageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;

@Component
@AllArgsConstructor
public class ClientHandler {
    private final MedicineService medicineService;
    private final UsageService usageService;

    public void handle(Socket socket) {

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter writer = new PrintWriter(
                    socket.getOutputStream(),
                    true
            );

            String request = reader.readLine();

            System.out.println("Client gửi: " + request);

            String response;

            if ("GET_MEDICINES".equals(request)) {

                List<Medicine> medicines = medicineService.getAll();

                if (medicines.isEmpty()) {

                    response = "EMPTY";

                } else {

                    StringBuilder result = new StringBuilder();

                    for (Medicine medicine : medicines) {

                        if (result.length() > 0) {
                            result.append("||");
                        }

                        result.append(medicine.getId())
                                .append("|")
                                .append(medicine.getName())
                                .append("|")
                                .append(medicine.getPrice())
                                .append("|")
                                .append(medicine.getType())
                                .append("|")
                                .append(medicine.getUsage().getName());
                    }

                    response = result.toString();
                }

            } else if ("GET_USAGES".equals(request)) {

                List<Usage> usages = usageService.getAll();

                if (usages.isEmpty()) {

                    response = "EMPTY";

                } else {

                    StringBuilder result = new StringBuilder();

                    for (Usage usage : usages) {

                        if (result.length() > 0) {
                            result.append("||");
                        }

                        result.append(usage.getId())
                                .append("|")
                                .append(usage.getName())
                                .append("|")
                                .append(usage.getDescription());
                    }

                    response = result.toString();
                }

            } else if (request.startsWith("ADD_USAGE|")) {

                String[] data = request.split("\\|");

                if (data.length >= 4) {

                    Usage usage = new Usage(
                            data[1],
                            data[2],
                            data[3]
                    );

                    usageService.save(usage);

                    response = "Thêm công dụng thành công";

                } else {

                    response = "Dữ liệu không hợp lệ";
                }

            } else if (request.startsWith("ADD_MEDICINE|")) {

                String[] data = request.split("\\|");

                if (data.length >= 6) {

                    String id = data[1];
                    String name = data[2];
                    BigDecimal price = new BigDecimal(data[3]);
                    String type = data[4];
                    String usageId = data[5];

                    Usage usage = usageService.getById(usageId);

                    if (usage == null) {

                        response = "Không tìm thấy công dụng";

                    } else {

                        Medicine medicine = new Medicine(
                                id,
                                name,
                                price,
                                type,
                                usage
                        );

                        medicineService.save(medicine);

                        response = "Thêm thuốc thành công";
                    }

                } else {

                    response = "Dữ liệu không hợp lệ";
                }

            } else if (request.startsWith("UPDATE_MEDICINE|")) {

                String[] data = request.split("\\|");

                if (data.length >= 6) {

                    String id = data[1];
                    String name = data[2];
                    BigDecimal price = new BigDecimal(data[3]);
                    String type = data[4];
                    String usageId = data[5];

                    Medicine medicine = medicineService.getById(id);
                    Usage usage = usageService.getById(usageId);

                    if (medicine == null) {

                        response = "Không tìm thấy thuốc";

                    } else if (usage == null) {

                        response = "Không tìm thấy công dụng";

                    } else {

                        medicine.setName(name);
                        medicine.setPrice(price);
                        medicine.setType(type);
                        medicine.setUsage(usage);

                        medicineService.save(medicine);

                        response = "Cập nhật thuốc thành công";
                    }

                } else {

                    response = "Dữ liệu không hợp lệ";
                }

            } else if (request.startsWith("DELETE_MEDICINE|")) {

                String[] data = request.split("\\|");

                String id = data[1];

                Medicine medicine = medicineService.getById(id);

                if (medicine == null) {

                    response = "Không tìm thấy thuốc";

                } else {

                    medicineService.delete(id);

                    response = "Xóa thuốc thành công";
                }

            } else if (request.startsWith("DELETE_USAGE|")) {

                String[] data = request.split("\\|");

                String id = data[1];

                Usage usage = usageService.getById(id);

                if (usage == null) {

                    response = "Không tìm thấy công dụng";

                } else if (medicineService.hasMedicineByUsage(id)) {

                    response = "Không thể xóa công dụng vì đang có thuốc sử dụng";

                } else {

                    usageService.delete(id);

                    response = "Xóa công dụng thành công";
                }

            } else if (request.startsWith("UPDATE_USAGE|")) {

                String[] data = request.split("\\|");

                if (data.length >= 4) {

                    String id = data[1];
                    String name = data[2];
                    String description = data[3];

                    Usage usage = usageService.getById(id);

                    if (usage == null) {

                        response = "Không tìm thấy công dụng";

                    } else {

                        usage.setName(name);
                        usage.setDescription(description);

                        usageService.save(usage);

                        response = "Cập nhật công dụng thành công";
                    }

                } else {

                    response = "Dữ liệu không hợp lệ";
                }

            } else if (request.startsWith("GET_USAGE|")) {

                String[] data = request.split("\\|");

                String id = data[1];

                Usage usage = usageService.getById(id);

                if (usage == null) {

                    response = "EMPTY";

                } else {

                    response = usage.getId()
                            + "|"
                            + usage.getName()
                            + "|"
                            + usage.getDescription();
                }

            } else if (request.startsWith("GET_MEDICINE|")) {

                String[] data = request.split("\\|");

                String id = data[1];

                Medicine medicine = medicineService.getById(id);

                if (medicine == null) {

                    response = "EMPTY";

                } else {

                    response = medicine.getId()
                            + "|"
                            + medicine.getName()
                            + "|"
                            + medicine.getPrice()
                            + "|"
                            + medicine.getType()
                            + "|"
                            + medicine.getUsage().getId();
                }

            } else if (request.startsWith("SEARCH_MEDICINE|")) {

                String[] data = request.split("\\|", -1);

                String priceText = data[1];
                String type = data[2];
                String usageId = data[3];

                BigDecimal price = null;

                if (!priceText.isEmpty()) {
                    price = new BigDecimal(priceText);
                }

                List<Medicine> medicines = medicineService.search(
                        price,
                        type,
                        usageId
                );

                if (medicines.isEmpty()) {

                    response = "EMPTY";

                } else {

                    StringBuilder result = new StringBuilder();

                    for (Medicine medicine : medicines) {

                        if (result.length() > 0) {
                            result.append("||");
                        }

                        result.append(medicine.getId())
                                .append("|")
                                .append(medicine.getName())
                                .append("|")
                                .append(medicine.getPrice())
                                .append("|")
                                .append(medicine.getType())
                                .append("|")
                                .append(medicine.getUsage().getName());
                    }

                    response = result.toString();
                }

            } else if (request.startsWith("SEARCH_USAGE|")) {

                String[] data = request.split("\\|", -1);

                String name = data[1];

                List<Usage> usages = usageService.searchByName(name);

                if (usages.isEmpty()) {

                    response = "EMPTY";

                } else {

                    StringBuilder result = new StringBuilder();

                    for (Usage usage : usages) {

                        if (result.length() > 0) {
                            result.append("||");
                        }

                        result.append(usage.getId())
                                .append("|")
                                .append(usage.getName())
                                .append("|")
                                .append(usage.getDescription());
                    }

                    response = result.toString();
                }

            } else if ("HELLO".equals(request)) {

                response = "TCP Server hoạt động bình thường";

            } else {

                response = "Server nhận được: " + request;
            }

            writer.println(response);

            socket.close();

        } catch (Exception e) {

            System.out.println(
                    "Lỗi Client Handler: " + e.getMessage()
            );
        }
    }
}
