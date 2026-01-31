package com.cananalyzer.service;

import com.cananalyzer.model.CANMessage;
import com.cananalyzer.model.ECU;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CANSimulatorService {

    private final Random random = new Random();
    private final ConcurrentHashMap<Integer, AtomicLong> messageCount = new ConcurrentHashMap<>();
    
    private static final String[] SIGNAL_TYPES = {
        "SPEED", "RPM", "TEMP", "PRESSURE", "VOLTAGE", "STATUS", "TORQUE", "ANGLE"
    };

    public CANMessage generateMessage() {
        ECU ecu = ECU.values()[random.nextInt(ECU.values().length)];
        int id = ecu.getMinId() + random.nextInt(ecu.getMaxId() - ecu.getMinId());
        int dlc = random.nextInt(8) + 1;
        int[] data = new int[dlc];
        
        for (int i = 0; i < dlc; i++) {
            data[i] = random.nextInt(256);
        }

        messageCount.computeIfAbsent(id, k -> new AtomicLong()).incrementAndGet();

        return CANMessage.builder()
                .timestamp(System.currentTimeMillis())
                .id(id)
                .idHex(String.format("0x%03X", id))
                .dlc(dlc)
                .data(data)
                .dataHex(formatDataHex(data))
                .ecuName(ecu.getName())
                .signalType(SIGNAL_TYPES[random.nextInt(SIGNAL_TYPES.length)])
                .build();
    }

    private String formatDataHex(int[] data) {
        StringBuilder sb = new StringBuilder();
        for (int b : data) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

    public ConcurrentHashMap<Integer, AtomicLong> getMessageCount() {
        return messageCount;
    }

    public void resetStats() {
        messageCount.clear();
    }
}
