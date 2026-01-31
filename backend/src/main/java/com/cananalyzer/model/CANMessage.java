package com.cananalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CANMessage {
    private long timestamp;
    private int id;
    private String idHex;
    private int dlc;
    private int[] data;
    private String dataHex;
    private String ecuName;
    private String signalType;
}
