package com.ezfarm.ezfarmback.remote.dto;

import com.ezfarm.ezfarmback.remote.domain.OnOff;
import com.ezfarm.ezfarmback.remote.domain.Remote;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RemoteResponse {

    private Long id;
    private OnOff water;
    private float temperature;
    private OnOff illuminance;
    private OnOff co2;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public static RemoteResponse of(Remote remote) {
        return new RemoteResponse(
            remote.getId(),
            remote.getWater(),
            remote.getTemperature(),
            remote.getIlluminance(),
            remote.getCo2(),
            remote.getCreatedDate(),
            remote.getUpdatedDate()
        );
    }
}
