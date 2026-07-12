package models;

import enums.GateType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class Gate {
    private String gateId;
    private GateType gateType;
}
