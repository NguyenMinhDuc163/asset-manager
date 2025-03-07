package com.ptit.asset.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryCreateRequestDTO {

    @NotNull
    private Instant time;
    @NotNull
    private Instant startTime;
    @NotNull
    private Instant endTime;

}
