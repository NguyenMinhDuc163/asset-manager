## asset_manager

## Dừng tất cả các container trong docker-compose
docker-compose down

## Khởi động lại container và build lại hình ảnh nếu có thay đổi
docker-compose up --build


## sua file JDBCTemplateConfig.java
neu chay localhost dung localhost neu chạy docker dung sqlserver_container
dataSourceBuilder.url("jdbc:sqlserver://sqlserver_container;databaseName="+"asset_db");

## sau khi lên chạy lệnh sau trong datagrip
```bash
CREATE DATABASE [asset_db]
```

## chạy container asset_manager_app để tạo bảng qua ORM

## sau khi lên thì chạy file ./db/ddl/sql trong datagrip

## chạy FE
