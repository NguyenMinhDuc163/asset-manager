package com.ptit.asset.repository.data;

import java.util.Date;

public interface BackupRestore {

    interface VersionBackup {
        Long getId();
        Date getBackupStartDate();
        Date getBackupEndDate();
        Long getPosition();
    }
}
