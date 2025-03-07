package com.ptit.asset.repository;

import com.ptit.asset.domain.User;
import com.ptit.asset.repository.data.BackupRestore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackupRestoreRepository extends JpaRepository <User,Long> {

    @Query(value =
            "SELECT BK.backup_set_id AS ID,\n" +
                    "\t   BK.backup_start_date AS backupStartDate,\n" +
                    "\t   BK.backup_finish_date AS backupEndDate,\n" +
                    "\t   BK.position AS Position\n" +
                    "FROM msdb.dbo.backupset AS BK\n" +
                    "WHERE BK.database_name = :database_name",
            nativeQuery = true)
    List<BackupRestore.VersionBackup> fetchAllVersionBackup(@Param("database_name") String name);
}
