---------- tạo device -----------------
USE Master
Go
EXEC Sp_addumpdevice 'disk' , 'DEVICE_assetManagement','E:\MyBackup\DEVICE_assetManagement.bak'

----------- tạo backup -----------------
 -- MẶC ĐỊNH LÀ FULL BACKUP
BACKUP DATABASE TEST_BACKUP TO DISK = 'E:\MyBackup\DEVICE_assetManagement.bak' -- Cách 1

BACKUP DATABASE TEST_BACKUP TO DEVICE_assetManagement --Cách 2



SELECT BK.backup_set_id AS ID,
	   BK.backup_start_date AS backupStartDate,
	   BK.backup_finish_date AS backupEndDate,
	   BK.position AS Position
FROM msdb.dbo.backupset AS BK
WHERE BK.database_name = 'TEST_BACKUP'


SELECT * FROM msdb.dbo.backupset WHERE database_name = 'TEST_BACKUP'

---------- xóa device ------------------

EXEC sp_dropdevice 'DEVICE_demo', 'delfile'  -- xoa luôn file trên disk
----------- restore về thòi điểm đã đc sao lưu --------------

ALTER DATABASE TEST_BACKUP SET SINGLE_USER WITH ROLLBACK IMMEDIATE -- bật mode 1 user

RESTORE DATABASE TEST_BACKUP FROM DEVICE_assetManagement WITH FILE= 1 ,REPLACE

ALTER DATABASE TEST_BACKUP SET MULTI_USER














---------- restore về thời điểm chưa đc sao lưu --------------

 ALTER DATABASE demo SET SINGLE_USER WITH ROLLBACK IMMEDIATE

 BACKUP LOG demo TO DISK = 'E:\HK8\ChuyenDe_CNPM_HTTT\Project\back-res\DEVICE_demo.trn' WITH INIT, NORECOVERY; -- xóa file nhật ký

 USE tempdb SET DATEFORMAT YMD
 RESTORE DATABASE demo FROM DEVICE_demo WITH FILE = 1, NORECOVERY
 RESTORE DATABASE demo FROM DISK = 'E:\HK8\ChuyenDe_CNPM_HTTT\Project\back-res\DEVICE_demo.trn' WITH STOPAT = '2020-05-28 10:16:50 AM', RECOVERY

 ALTER DATABASE demo SET MULTI_USER