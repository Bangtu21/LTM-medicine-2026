package com.example.medicinemanagement.controller;

import com.example.medicinemanagement.entity.Medicine;
import com.example.medicinemanagement.entity.Usage;
import com.example.medicinemanagement.tcp.TcpClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class MedicineController {
    private final TcpClient tcpClient;

    @GetMapping("/medicines")
    public String medicines(Model model) {
        String response = tcpClient.send("GET_MEDICINES");

        List<Medicine> medicines = parseMedicines(response);

        model.addAttribute("medicines", medicines);
        model.addAttribute("message", response);

        return "medicine-list";
    }

    @GetMapping("/medicines/add")
    public String addForm(Model model) {
        String response = tcpClient.send("GET_USAGES");

        List<Usage> usages = new ArrayList<>();

        if (!"EMPTY".equals(response)) {
            String[] usageList = response.split("\\|\\|");
            for (String item : usageList) {
                String[] data = item.split("\\|");
                if (data.length >= 3) {
                    Usage usage = new Usage(
                            data[0],
                            data[1],
                            data[2]
                    );
                    usages.add(usage);
                }
            }
        }

        model.addAttribute("usages", usages);
        return "medicine-form";
    }

    @GetMapping("/medicines/edit")
    public String editForm(
            @RequestParam String id,
            Model model
    ) {
        String response = tcpClient.send("GET_MEDICINE|" + id);
        String[] data = response.split("\\|");
        if (data.length >= 5) {
            Medicine medicine = new Medicine();

            medicine.setId(data[0]);
            medicine.setName(data[1]);
            medicine.setPrice(new java.math.BigDecimal(data[2]));
            medicine.setType(data[3]);

            Usage usage = new Usage();
            usage.setId(data[4]);

            medicine.setUsage(usage);

            model.addAttribute("medicine", medicine);
        }

        String usageResponse = tcpClient.send("GET_USAGES");
        List<Usage> usages = new ArrayList<>();
        if (!"EMPTY".equals(usageResponse)) {
            String[] usageList = usageResponse.split("\\|\\|");
            for (String item : usageList) {
                String[] usageData = item.split("\\|");
                if (usageData.length >= 3) {
                    Usage usage = new Usage(
                            usageData[0],
                            usageData[1],
                            usageData[2]
                    );
                    usages.add(usage);
                }
            }
        }

        model.addAttribute("usages", usages);
        return "medicine-edit";
    }

    @PostMapping("/medicines/add")
    public String add(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String price,
            @RequestParam String type,
            @RequestParam String usageId
    ) {
        String message = "ADD_MEDICINE|"
                + id + "|"
                + name + "|"
                + price + "|"
                + type + "|"
                + usageId;

        tcpClient.send(message);
        return "redirect:/medicines";
    }

    @GetMapping("/medicines/search")
    public String search(
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String usageId,
            Model model
    ) {
        String message = "SEARCH_MEDICINE|"
                + (price == null ? "" : price) + "|"
                + (type == null ? "" : type) + "|"
                + (usageId == null ? "" : usageId);

        String response = tcpClient.send(message);

        List<Medicine> medicines = parseMedicines(response);

        model.addAttribute("medicines", medicines);
        model.addAttribute("message", response);

        return "medicine-list";
    }

    @GetMapping("/medicines/search-form")
    public String searchForm() {
        return "medicine-search";
    }

    private List<Medicine> parseMedicines(String response) {
        List<Medicine> medicines = new ArrayList<>();
        if (response == null || "EMPTY".equals(response)) {
            return medicines;
        }
        String[] medicineList = response.split("\\|\\|");
        for (String item : medicineList) {
            String[] data = item.split("\\|");
            if (data.length >= 5) {
                Medicine medicine = new Medicine();

                medicine.setId(data[0]);
                medicine.setName(data[1]);
                medicine.setPrice(new java.math.BigDecimal(data[2]));
                medicine.setType(data[3]);

                Usage usage = new Usage();
                usage.setName(data[4]);

                medicine.setUsage(usage);

                medicines.add(medicine);
            }
        }

        return medicines;
    }

    @PostMapping("/medicines/edit")
    public String edit(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String price,
            @RequestParam String type,
            @RequestParam String usageId
    ) {

        String message = "UPDATE_MEDICINE|"
                + id + "|"
                + name + "|"
                + price + "|"
                + type + "|"
                + usageId;

        tcpClient.send(message);

        return "redirect:/medicines";
    }

    @GetMapping("/medicines/delete")
    public String delete(@RequestParam String id) {

        String message = "DELETE_MEDICINE|" + id;

        tcpClient.send(message);

        return "redirect:/medicines";
    }
}
