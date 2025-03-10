# asset_manager

# Dừng tất cả các container trong docker-compose
docker-compose down

# Khởi động lại container và build lại hình ảnh nếu có thay đổi
docker-compose up --build

# Copy file ddl.sql vào container có tên sqlserver_container
docker cp db/ddl.sql sqlserver_container:/ddl.sql

# Mở SQL Server command line, kết nối đến localhost với user SA và mật khẩu "password"
sqlcmd -S localhost -U SA -P "password" -d master

# Chạy file SQL ddl.sql trên database master
sqlcmd -S localhost -U SA -P "password" -d master -i ./db/ddl.sql
