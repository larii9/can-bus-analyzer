package com.cananalyzer.controller;

import com.cananalyzer.model.ECU;
import com.cananalyzer.service.CANSimulatorService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CANController {

    private final CANSimulatorService simulatorService;

    public CANController(CANSimulatorService simulatorService) {
        this.simulatorService = simulatorService;
    }

    @GetMapping("/ecus")
    public List<Map<String, Object>> getECUs() {
        List<Map<String, Object>> ecus = new ArrayList<>();
        for (ECU ecu : ECU.values()) {
            Map<String, Object> ecuMap = new HashMap<>();
            ecuMap.put("name", ecu.getName());
            ecuMap.put("minId", String.format("0x%03X", ecu.getMinId()));
            ecuMap.put("maxId", String.format("0x%03X", ecu.getMaxId()));
            ecus.add(ecuMap);
        }
        return ecus;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        var counts = simulatorService.getMessageCount();
        
        stats.put("totalMessages", counts.values().stream().mapToLong(a -> a.get()).sum());
        stats.put("uniqueIds", counts.size());
        
        List<Map<String, Object>> topIds = new ArrayList<>();
        counts.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().get(), a.getValue().get()))
                .limit(10)
                .forEach(e -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("id", String.format("0x%03X", e.getKey()));
                    entry.put("count", e.getValue().get());
                    topIds.add(entry);
                });
        stats.put("topIds", topIds);
        
        return stats;
    }

    @PostMapping("/reset")
    public Map<String, String> resetStats() {
        simulatorService.resetStats();
        return Map.of("status", "ok");
    }
}
