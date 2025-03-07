package com.ptit.asset.service.manager;

import com.ptit.asset.dto.response.BackupVersionResponseDTO;
import io.vavr.control.Try;

import java.util.List;

public interface BackupRestoreManagement {

    List<BackupVersionResponseDTO> fetchAllVersion();

    Try<Boolean> createBackup();

    Try<Boolean> restoreDatabase(Long versionBackup);

}
