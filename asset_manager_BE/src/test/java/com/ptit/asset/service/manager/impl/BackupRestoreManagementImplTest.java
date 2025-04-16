package com.ptit.asset.service.manager.impl;

import com.ptit.asset.dto.response.BackupVersionResponseDTO;
import com.ptit.asset.repository.BackupRestoreRepository;
import com.ptit.asset.repository.data.BackupRestore;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BackupRestoreManagementImplTest {

    @Mock private JdbcTemplate jdbcTemplate;
    @Mock private BackupRestoreRepository backupRestoreRepository;
    @InjectMocks private BackupRestoreManagementImpl backupRestoreManagement;

    private String schemaName = "test_schema";
    private String deviceName = "test_device";

    @BeforeEach
    void setUp() {
        // Initialize the service with mocked dependencies and configuration
        backupRestoreManagement = new BackupRestoreManagementImpl();
        setField(backupRestoreManagement, "schemaName", schemaName);
        setField(backupRestoreManagement, "deviceName", deviceName);
        setField(backupRestoreManagement, "jdbcTemplate", jdbcTemplate);
        setField(backupRestoreManagement, "backupRestoreRepository", backupRestoreRepository);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }

    // STT 1: Tests for fetchAllVersion
    @Test
    void fetchAllVersion_success() {
        /*
         * Test Case: fetchAllVersion_success
         * Description: Tests the fetchAllVersion method when the repository successfully returns a list of backup versions.
         * Input:
         *   - Mocked backupRestoreRepository.fetchAllVersionBackup("test_schema") returns a list with one VersionBackup object.
         *   - VersionBackup has ID=1, backup start date=2023-01-01 10:00:00, end date=2023-01-01 11:00:00, position=1.
         * Output:
         *   - List<BackupVersionResponseDTO> containing one DTO mapped from the VersionBackup.
         * Expected Output:
         *   - Result list has size 1.
         *   - DTO has ID=1, backup_start_date contains "2023", backup_finish_date contains "2023", position=1.
         *   - Repository method is called once with schemaName.
         */
        Date startDate = new Date(123, 0, 1, 10, 0, 0); // 2023-01-01 10:00:00
        Date endDate = new Date(123, 0, 1, 11, 0, 0);   // 2023-01-01 11:00:00
        BackupRestore.VersionBackup version = new BackupRestore.VersionBackup() {
            @Override public Long getId() { return 1L; }
            @Override public Date getBackupStartDate() { return startDate; }
            @Override public Date getBackupEndDate() { return endDate; }
            @Override public Long getPosition() { return 1L; }
        };
        List<BackupRestore.VersionBackup> versions = Collections.singletonList(version);

        when(backupRestoreRepository.fetchAllVersionBackup(schemaName)).thenReturn(versions);

        List<BackupVersionResponseDTO> result = backupRestoreManagement.fetchAllVersion();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getBackup_start_date()).contains("2023");
        assertThat(result.get(0).getBackup_finish_date()).contains("2023");
        assertThat(result.get(0).getPosition()).isEqualTo(1L);
        verify(backupRestoreRepository).fetchAllVersionBackup(schemaName);
    }

    @Test
    void fetchAllVersion_success_empty() {
        /*
         * Test Case: fetchAllVersion_success_empty
         * Description: Tests the fetchAllVersion method when the repository returns an empty list of backup versions.
         * Input:
         *   - Mocked backupRestoreRepository.fetchAllVersionBackup("test_schema") returns an empty list.
         * Output:
         *   - Empty List<BackupVersionResponseDTO>.
         * Expected Output:
         *   - Result list is empty.
         *   - Repository method is called once with schemaName.
         */
        when(backupRestoreRepository.fetchAllVersionBackup(schemaName)).thenReturn(Collections.emptyList());

        List<BackupVersionResponseDTO> result = backupRestoreManagement.fetchAllVersion();

        assertThat(result).isEmpty();
        verify(backupRestoreRepository).fetchAllVersionBackup(schemaName);
    }

    // STT 2: Tests for createBackup
    @Test
    void createBackup_success() {
        /*
         * Test Case: createBackup_success
         * Description: Tests the createBackup method when the backup operation succeeds without errors.
         * Input:
         *   - Mocked jdbcTemplate.execute() for the backup SQL command ("USE Master BACKUP DATABASE test_schema TO test_device") does nothing (no exception).
         *   - Mocked jdbcTemplate.execute() for switching back to schema ("USE test_schema") does nothing.
         * Output:
         *   - Try<Boolean> with success value true.
         * Expected Output:
         *   - Result is Try.success(true).
         *   - JdbcTemplate executes the backup SQL command once.
         *   - JdbcTemplate executes the schema switch command once.
         */
        doNothing().when(jdbcTemplate).execute(anyString());

        Try<Boolean> result = backupRestoreManagement.createBackup();

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(jdbcTemplate).execute("USE Master BACKUP DATABASE test_schema TO test_device");
        verify(jdbcTemplate).execute("USE test_schema");
    }

    @Test
    void createBackup_failure() {
        /*
         * Test Case: createBackup_failure
         * Description: Tests the createBackup method when the backup operation fails due to a database error.
         * Input:
         *   - Mocked jdbcTemplate.execute() for the backup SQL command throws a DataAccessException with message "Backup error".
         * Output:
         *   - Try<Boolean> with failure containing the DataAccessException.
         * Expected Output:
         *   - Result is Try.failure.
         *   - Exception is a DataAccessException with message containing "Backup error".
         *   - JdbcTemplate executes the backup SQL command once.
         *   - JdbcTemplate does not execute the schema switch command ("USE test_schema").
         */
        doThrow(new DataAccessException("Backup error") {})
                .when(jdbcTemplate).execute("USE Master BACKUP DATABASE test_schema TO test_device");

        Try<Boolean> result = backupRestoreManagement.createBackup();

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).isInstanceOf(DataAccessException.class);
        assertThat(result.getCause().getMessage()).contains("Backup error");
        verify(jdbcTemplate).execute("USE Master BACKUP DATABASE test_schema TO test_device");
        verify(jdbcTemplate, never()).execute("USE test_schema");
    }

    // STT 3: Tests for restoreDatabase
    @Test
    void restoreDatabase_success() {
        /*
         * Test Case: restoreDatabase_success
         * Description: Tests the restoreDatabase method when the restore operation succeeds without errors.
         * Input:
         *   - versionBackup = 1L.
         *   - Mocked jdbcTemplate.execute() for the restore SQL command (ALTER DATABASE, RESTORE, SET MULTI_USER) does nothing.
         *   - Mocked jdbcTemplate.execute() for switching back to schema ("USE test_schema") does nothing.
         * Output:
         *   - Try<Boolean> with success value true.
         * Expected Output:
         *   - Result is Try.success(true).
         *   - JdbcTemplate executes the restore SQL command once.
         *   - JdbcTemplate executes the schema switch command once.
         */
        Long versionBackup = 1L;
        doNothing().when(jdbcTemplate).execute(anyString());

        Try<Boolean> result = backupRestoreManagement.restoreDatabase(versionBackup);

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isTrue();
        verify(jdbcTemplate).execute(
                "USE Master ALTER DATABASE test_schema SET SINGLE_USER WITH ROLLBACK IMMEDIATE " +
                        "RESTORE DATABASE test_schema FROM test_device WITH FILE=1 ,REPLACE " +
                        "ALTER DATABASE test_schema SET MULTI_USER"
        );
        verify(jdbcTemplate).execute("USE test_schema");
    }

    @Test
    void restoreDatabase_failure() {
        /*
         * Test Case: restoreDatabase_failure
         * Description: Tests the restoreDatabase method when the restore operation fails due to a database error.
         * Input:
         *   - versionBackup = 1L.
         *   - Mocked jdbcTemplate.execute() for the restore SQL command throws a DataAccessException with message "Restore error".
         * Output:
         *   - Try<Boolean> with failure containing the DataAccessException.
         * Expected Output:
         *   - Result is Try.failure.
         *   - Exception is a DataAccessException with message containing "Restore error".
         *   - JdbcTemplate executes the restore SQL command once.
         *   - JdbcTemplate does not execute the schema switch command ("USE test_schema").
         */
        Long versionBackup = 1L;
        doThrow(new DataAccessException("Restore error") {})
                .when(jdbcTemplate).execute(
                        "USE Master ALTER DATABASE test_schema SET SINGLE_USER WITH ROLLBACK IMMEDIATE " +
                                "RESTORE DATABASE test_schema FROM test_device WITH FILE=1 ,REPLACE " +
                                "ALTER DATABASE test_schema SET MULTI_USER"
                );

        Try<Boolean> result = backupRestoreManagement.restoreDatabase(versionBackup);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause()).isInstanceOf(DataAccessException.class);
        assertThat(result.getCause().getMessage()).contains("Restore error");
        verify(jdbcTemplate).execute(
                "USE Master ALTER DATABASE test_schema SET SINGLE_USER WITH ROLLBACK IMMEDIATE " +
                        "RESTORE DATABASE test_schema FROM test_device WITH FILE=1 ,REPLACE " +
                        "ALTER DATABASE test_schema SET MULTI_USER"
        );
        verify(jdbcTemplate, never()).execute("USE test_schema");
    }
}