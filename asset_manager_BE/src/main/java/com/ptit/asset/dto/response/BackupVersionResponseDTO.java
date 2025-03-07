package com.ptit.asset.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BackupVersionResponseDTO {

    private Long id;
    private String backup_start_date;
    private String backup_finish_date;
    private Long position;
}
