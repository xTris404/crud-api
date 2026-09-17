# Spring Boot CRUD API + AJAX (Category & Product)

Project hoàn chỉnh minh họa **RESTful API** + **AJAX** cho CRUD bảng **Category** và **Product**, hỗ trợ upload file ảnh.

## Công nghệ

- Spring Boot 3.1.5
- Spring Data JPA + H2 (in-memory) / MySQL
- Springdoc OpenAPI (Swagger UI)
- Thymeleaf + Bootstrap 5 + jQuery (AJAX)
- File upload (Commons IO)

## Cấu trúc chính

```
src/main/java/vn/iotstar/
├── entity/          # Category, Product
├── repository/      # JpaRepository
├── service/         # Service + Impl + Storage
├── model/           # Response wrapper
├── controllers/api/ # CategoryAPIController, ProductApiController, FileController
├── config/          # StorageProperties
└── Exception/       # StorageException
```

## Chạy project

### Yêu cầu
- JDK 17+
- Maven 3.8+

### Cách chạy

```bash
cd springboot-crud-api
mvn spring-boot:run
```

Hoặc import vào IntelliJ / Eclipse rồi Run `SpringbootCrudApiApplication`.

### Truy cập

| URL | Mô tả |
|-----|-------|
| http://localhost:8080 | Trang chủ |
| http://localhost:8080/categories | CRUD Category (AJAX) |
| http://localhost:8080/products | CRUD Product (AJAX) |
| http://localhost:8080/swagger-ui.html | Swagger UI |
| http://localhost:8080/h2-console | H2 Console (JDBC URL: `jdbc:h2:mem:cruddb`) |

## API Endpoints

### Category
- `GET /api/category` → danh sách
- `POST /api/category/addCategory` (form-data: categoryName, icon)
- `PUT /api/category/updateCategory` (form-data: categoryId, categoryName, icon)
- `DELETE /api/category/deleteCategory?categoryId=`

### Product
- `GET /api/product` → danh sách
- `POST /api/product/addProduct` (form-data: productName, imageFile, unitPrice, discount, description, categoryId, quantity, status)
- `PUT /api/product/updateProduct` (tương tự + productId)
- `DELETE /api/product/deleteProduct?productId=`

## Lưu ý

- Ảnh upload lưu vào thư mục `uploads/` (tự tạo khi chạy).
- Để dùng MySQL: sửa `application.properties` và thêm dependency MySQL (đã có sẵn).
- Project dùng Response wrapper `{ status, message, body }` như trong tài liệu giảng viên.
