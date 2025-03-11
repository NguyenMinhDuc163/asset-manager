# asset_manager

# Dừng tất cả các container trong docker-compose
docker-compose down

# Khởi động lại container và build lại hình ảnh nếu có thay đổi
docker-compose up --build

# sau khi lên chạy lệnh sau trong datagrip
```bash
CREATE DATABASE [asset_db]
USE [asset_db]
ALTER DATABASE asset_db SET MULTI_USER
EXEC Sp_addumpdevice 'disk', 'DEVICE_assetManagement', '/var/opt/mssql/backup/assetManagement.bak'
BACKUP DATABASE asset_db TO DISK = '/var/opt/mssql/backup/assetManagement.bak'
```

# chạy container asset_manager_app để tạo bảng qua ORM

# sau khi lên thì chạy file ./db/ddl/sql trong datagrip

# chạy FE
