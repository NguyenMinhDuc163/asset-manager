package com.ptit.asset.service.manager.impl;

import com.ptit.asset.dto.response.BackupVersionResponseDTO;
import com.ptit.asset.repository.BackupRestoreRepository;
import com.ptit.asset.repository.data.BackupRestore;
import com.ptit.asset.service.manager.BackupRestoreManagement;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BackupRestoreManagementImpl implements BackupRestoreManagement {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.schema-name}")
    private String schemaName;
    @Value("${spring.device-name}")
    private String deviceName;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BackupRestoreRepository backupRestoreRepository;

    @Override
    public List<BackupVersionResponseDTO> fetchAllVersion() {
        List<BackupRestore.VersionBackup> versionBackupList = backupRestoreRepository.fetchAllVersionBackup(schemaName);
        return versionBackupList.stream()
        .map(element -> new BackupVersionResponseDTO(
                element.getId(),
                element.getBackupStartDate().toString(),
                element.getBackupEndDate().toString(),
                element.getPosition()
        ))
        .sorted(Comparator.comparing(BackupVersionResponseDTO::getPosition).reversed())
        .collect(Collectors.toList());
    }

    @Override
    public Try<Boolean> createBackup() {
        logger.info("==> Starting create backup for schema : {}",schemaName);
        String query = "USE Master " +
                "BACKUP DATABASE " +schemaName+ " TO " + deviceName;
        jdbcTemplate.execute(query);
        jdbcTemplate.execute("USE "+schemaName);
        return Try.of(() -> true);
    }

    @Override
    public Try<Boolean> restoreDatabase(Long versionBackup) {
        logger.info("==> Starting process restore database back to version: {}",versionBackup);
        String query
                = "USE Master " +
                "ALTER DATABASE " +schemaName+ " SET SINGLE_USER WITH ROLLBACK IMMEDIATE " +
                "RESTORE DATABASE " +schemaName+ " FROM " +deviceName+ " WITH FILE="+versionBackup+" ,REPLACE " +
                "ALTER DATABASE " +schemaName+ " SET MULTI_USER";

        jdbcTemplate.execute(query);
        jdbcTemplate.execute("USE "+schemaName);
        return Try.of(() -> true);
    }
}
