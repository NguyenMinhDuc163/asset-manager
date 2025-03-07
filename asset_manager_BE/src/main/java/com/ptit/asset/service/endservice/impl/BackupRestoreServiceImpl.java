package com.ptit.asset.service.endservice.impl;

import com.ptit.asset.dto.response.BackupVersionResponseDTO;
import com.ptit.asset.service.endservice.BackupRestoreService;
import com.ptit.asset.service.manager.BackupRestoreManagement;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackupRestoreServiceImpl implements BackupRestoreService {

    @Autowired
    private BackupRestoreManagement backupRestoreManagement;

    @Override
    public List<BackupVersionResponseDTO> fetchAllVersion() {
        return backupRestoreManagement.fetchAllVersion();
    }

    @Override
    public Try<Boolean> createBackup() {
        return backupRestoreManagement.createBackup();
    }

    @Override
    public Try<Boolean> restoreDatabase(Long versionBackup) {
        return backupRestoreManagement.restoreDatabase(versionBackup);
    }
}
