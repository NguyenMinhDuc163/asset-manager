package com.ptit.asset.dto.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonSerialize
public class EmptyDataObject {
    // no require anything here
}
