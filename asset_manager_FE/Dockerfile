# Sử dụng Node.js 16
FROM node:16

# Thiết lập thư mục làm việc
WORKDIR /app

# Copy package.json và package-lock.json để cài dependencies trước
COPY package.json package-lock.json ./

# Cài đặt dependencies
RUN npm install

# Copy toàn bộ mã nguồn vào container
COPY . .

# Mở port 3000
EXPOSE 3000

# Chạy ứng dụng React
CMD ["npm", "start"]
