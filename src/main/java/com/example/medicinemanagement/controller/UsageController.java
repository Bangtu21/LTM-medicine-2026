package com.example.medicinemanagement.controller;

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
public class UsageController {
    private final TcpClient tcpClient;

    @GetMapping("/usages")
    public String usages(Model model) {
        String response = tcpClient.send("GET_USAGES");

        List<Usage> usages = parseUsages(response);

        model.addAttribute("usages", usages);
        model.addAttribute("message", response);

        return "usage-list";
    }

    @GetMapping("/usages/add")
    public String addForm() {
        return "usage-form";
    }

    @PostMapping("/usages/add")
    public String add(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String description
    ) {
        String message = "ADD_USAGE|"
                + id + "|"
                + name + "|"
                + description;

        tcpClient.send(message);
        return "redirect:/usages";
    }

    @GetMapping("/usages/edit")
    public String editForm(
            @RequestParam String id,
            Model model
    ) {
        String response = tcpClient.send("GET_USAGE|" + id);

        String[] data = response.split("\\|");

        if (data.length >= 3) {
            Usage usage = new Usage(
                    data[0],
                    data[1],
                    data[2]
            );
            model.addAttribute("usage", usage);
        }
        return "usage-edit";
    }

    @PostMapping("/usages/edit")
    public String edit(
            @RequestParam String id,
            @RequestParam String name,
            @RequestParam String description
    ) {
        String message = "UPDATE_USAGE|"
                + id + "|"
                + name + "|"
                + description;

        tcpClient.send(message);
        return "redirect:/usages";
    }

    @GetMapping("/usages/delete")
    public String delete(
            @RequestParam String id,
            Model model
    ) {
        String message = "DELETE_USAGE|" + id;

        String response = tcpClient.send(message);

        String usageResponse = tcpClient.send("GET_USAGES");

        List<Usage> usages = parseUsages(usageResponse);

        model.addAttribute("usages", usages);
        model.addAttribute("message", response);

        return "usage-list";
    }

    private List<Usage> parseUsages(String response) {
        List<Usage> usages = new ArrayList<>();
        if (response == null || "EMPTY".equals(response)) {
            return usages;
        }
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

        return usages;
    }
    @GetMapping("/usages/search-form")
    public String searchForm() {
        return "usage-search";
    }

    @GetMapping("/usages/search")
    public String search(
            @RequestParam String name,
            Model model
    ) {

        String message = "SEARCH_USAGE|" + name;

        String response = tcpClient.send(message);

        List<Usage> usages = parseUsages(response);

        model.addAttribute("usages", usages);
        model.addAttribute("message", response);

        return "usage-list";
    }
}
