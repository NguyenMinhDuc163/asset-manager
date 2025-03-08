IF NOT EXISTS (SELECT 1 FROM dbo.roles WHERE role_name = 'ROLE_ADMIN')
    INSERT INTO dbo.roles (role_name) VALUES ('ROLE_ADMIN');

IF NOT EXISTS (SELECT 1 FROM dbo.roles WHERE role_name = 'ROLE_ACCOUNTANT')
    INSERT INTO dbo.roles (role_name) VALUES ('ROLE_ACCOUNTANT');

IF NOT EXISTS (SELECT 1 FROM dbo.roles WHERE role_name = 'ROLE_EMPLOYEE')
    INSERT INTO dbo.roles (role_name) VALUES ('ROLE_EMPLOYEE');

IF NOT EXISTS (SELECT 1 FROM dbo.roles WHERE role_name = 'ROLE_INSPECTOR')
    INSERT INTO dbo.roles (role_name) VALUES ('ROLE_INSPECTOR');


SET IDENTITY_INSERT dbo.groups ON;

IF NOT EXISTS (SELECT 1 FROM dbo.groups WHERE id = 1)
    INSERT INTO dbo.groups (id, created_at, updated_at, code, description)
    VALUES (1, CAST(N'2020-11-28 14:30:44.6260000' AS DateTime2), CAST(N'2020-11-28 14:30:44.6260000' AS DateTime2), N'NC-VKT', N'Nhà Cửa - Vật Kiến Trúc');

IF NOT EXISTS (SELECT 1 FROM dbo.groups WHERE id = 2)
    INSERT INTO dbo.groups (id, created_at, updated_at, code, description)
    VALUES (2, CAST(N'2020-11-28 14:31:08.3430000' AS DateTime2), CAST(N'2020-11-28 14:31:08.3430000' AS DateTime2), N'MM-TB', N'Máy Móc - Thiết Bị');

IF NOT EXISTS (SELECT 1 FROM dbo.groups WHERE id = 3)
    INSERT INTO dbo.groups (id, created_at, updated_at, code, description)
    VALUES (3, CAST(N'2020-11-28 14:31:30.5340000' AS DateTime2), CAST(N'2020-11-28 14:31:30.5340000' AS DateTime2), N'TB-PTVT', N'Thiết Bị - Phương Tiện Vận Tải');

IF NOT EXISTS (SELECT 1 FROM dbo.groups WHERE id = 4)
    INSERT INTO dbo.groups (id, created_at, updated_at, code, description)
    VALUES (4, CAST(N'2020-11-28 14:31:51.3240000' AS DateTime2), CAST(N'2020-11-28 14:31:51.3240000' AS DateTime2), N'TB-DCQL', N'Thiết Bị - Dụng Cụ Quản Lý');

IF NOT EXISTS (SELECT 1 FROM dbo.groups WHERE id = 5)
    INSERT INTO dbo.groups (id, created_at, updated_at, code, description)
    VALUES (5, CAST(N'2020-11-28 14:32:07.0760000' AS DateTime2), CAST(N'2020-11-28 14:32:07.0760000' AS DateTime2), N'TSCDHHK', N'Tài Sản Cố Định Hữu Hình Khác');

IF NOT EXISTS (SELECT 1 FROM dbo.groups WHERE id = 6)
    INSERT INTO dbo.groups (id, created_at, updated_at, code, description)
    VALUES (6, CAST(N'2020-11-28 14:32:21.7430000' AS DateTime2), CAST(N'2020-11-28 14:32:21.7430000' AS DateTime2), N'TSCDVH', N'Tài Sản Cố Định Vô Hình');

SET IDENTITY_INSERT dbo.groups OFF;
SET IDENTITY_INSERT dbo.category ON;

SET IDENTITY_INSERT dbo.category ON;

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 1)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (1, CAST(N'2020-11-28 14:33:17.0720000' AS DateTime2), CAST(N'2020-11-28 14:33:17.0720000' AS DateTime2), N'Nhà cửa Loại Kiên Cố', N'NCLKC', 1);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 2)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (2, CAST(N'2020-11-28 14:34:11.0060000' AS DateTime2), CAST(N'2020-11-28 14:34:11.0060000' AS DateTime2), N'Nhà cửa Loại Khác', N'NCLK', 1);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 3)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (3, CAST(N'2020-11-28 14:34:46.0100000' AS DateTime2), CAST(N'2020-11-28 14:34:46.0100000' AS DateTime2), N'Vật Kiến Trúc', N'VKT', 1);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 4)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (4, CAST(N'2020-11-28 14:35:06.4950000' AS DateTime2), CAST(N'2020-11-28 14:35:06.4950000' AS DateTime2), N'Máy Móc Thiết Bị - Động Lực', N'MMTB-DL', 2);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 5)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (5, CAST(N'2020-11-28 14:35:21.6260000' AS DateTime2), CAST(N'2020-11-28 14:35:21.6260000' AS DateTime2), N'Máy Móc Thiết Bị - Công Tác', N'MMTB-CT', 2);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 6)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (6, CAST(N'2020-11-28 14:35:38.0940000' AS DateTime2), CAST(N'2020-11-28 14:35:38.0940000' AS DateTime2), N'Dụng Cụ Làm Việc - Đo Lường Thí Nghiệm', N'DCLV-DLTN', 2);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 7)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (7, CAST(N'2020-11-28 14:36:07.3470000' AS DateTime2), CAST(N'2020-11-28 14:36:07.3470000' AS DateTime2), N'Phương Tiện Vận Tải Đường Bộ', N'PTVT-DB', 3);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 8)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (8, CAST(N'2020-11-28 14:36:29.3140000' AS DateTime2), CAST(N'2020-11-28 14:36:29.3140000' AS DateTime2), N'Máy Tính Xách Tay', N'MTXT', 4);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 9)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (9, CAST(N'2020-11-28 14:36:44.3210000' AS DateTime2), CAST(N'2020-11-28 14:36:44.3210000' AS DateTime2), N'Máy Chủ Server', N'MCSV', 4);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 10)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (10, CAST(N'2020-11-28 14:37:11.2650000' AS DateTime2), CAST(N'2020-11-28 14:37:11.2650000' AS DateTime2), N'Máy Chiếu', N'MC', 4);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 11)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (11, CAST(N'2020-11-28 14:37:25.1580000' AS DateTime2), CAST(N'2020-11-28 14:37:25.1580000' AS DateTime2), N'Máy Photocopy', N'MPTCP', 4);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 12)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (12, CAST(N'2020-11-28 14:37:37.1270000' AS DateTime2), CAST(N'2020-11-28 14:37:37.1270000' AS DateTime2), N'Máy Điều Hòa', N'MDH', 4);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 13)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (13, CAST(N'2020-11-28 14:37:55.5960000' AS DateTime2), CAST(N'2020-11-28 14:37:55.5960000' AS DateTime2), N'Các Phương Tiện Quản Lý Khác', N'CPTQLK', 4);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 14)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (14, CAST(N'2020-11-28 14:38:11.7870000' AS DateTime2), CAST(N'2020-11-28 14:38:11.7870000' AS DateTime2), N'Trạm Biến Áp', N'TBA', 5);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 15)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (15, CAST(N'2020-11-28 14:38:24.1840000' AS DateTime2), CAST(N'2020-11-28 14:38:24.1840000' AS DateTime2), N'Hệ Thống Khác', N'HTK', 5);

IF NOT EXISTS (SELECT 1 FROM dbo.category WHERE id = 16)
    INSERT INTO dbo.category (id, created_at, updated_at, description, name, group_id)
    VALUES (16, CAST(N'2020-11-28 14:38:41.1690000' AS DateTime2), CAST(N'2020-11-28 14:38:41.1690000' AS DateTime2), N'Đền Bù Đất', N'DBD', 6);

SET IDENTITY_INSERT dbo.category OFF;
