package com.cananalyzer.model;

import lombok.Getter;

@Getter
public enum ECU {
    ENGINE("Engine Control", 0x100, 0x1FF),
    TRANSMISSION("Transmission", 0x200, 0x2FF),
    ABS_ESP("ABS/ESP", 0x300, 0x3FF),
    INSTRUMENT("Instrument Cluster", 0x400, 0x4FF),
    BODY("Body Control", 0x500, 0x5FF);

    private final String name;
    private final int minId;
    private final int maxId;

    ECU(String name, int minId, int maxId) {
        this.name = name;
        this.minId = minId;
        this.maxId = maxId;
    }

    public static ECU fromId(int id) {
        for (ECU ecu : values()) {
            if (id >= ecu.minId && id <= ecu.maxId) {
                return ecu;
            }
        }
        return null;
    }
}
