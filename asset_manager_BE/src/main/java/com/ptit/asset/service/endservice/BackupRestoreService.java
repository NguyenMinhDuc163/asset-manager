package com.ptit.asset.service.endservice;

import com.ptit.asset.dto.response.BackupVersionResponseDTO;
import io.vavr.control.Try;

import java.util.List;

public interface BackupRestoreService {

    List<BackupVersionResponseDTO> fetchAllVersion();

    Try<Boolean> createBackup();

    Try<Boolean> restoreDatabase(Long versionBackup);

}
