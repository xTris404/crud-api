package vn.iotstar.controllers.api;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.model.Response;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.IStorageService;

@RestController
@RequestMapping("/api/product")
public class ProductApiController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IStorageService storageService;

    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<>(new Response(true, "Thành công", productService.findAll()), HttpStatus.OK);
    }

    @PostMapping("/getProduct")
    public ResponseEntity<?> getProduct(@Validated @RequestParam("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(new Response(true, "Thành công", product.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Response(false, "Không tìm thấy Product", null), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @Validated @RequestParam("discount") Double discount,
            @Validated @RequestParam("description") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @Validated @RequestParam("status") Short status) {

        if (productService.findByProductName(productName).isPresent()) {
            return new ResponseEntity<>(new Response(false, "Sản phẩm này đã tồn tại trong hệ thống", null),
                    HttpStatus.BAD_REQUEST);
        }

        Product product = new Product();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setStatus(status);
        product.setCreateDate(new Timestamp(new Date().getTime()));

        Optional<Category> cateOpt = categoryService.findById(categoryId);
        if (cateOpt.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Category không tồn tại", null), HttpStatus.BAD_REQUEST);
        }
        product.setCategory(cateOpt.get());

        if (imageFile != null && !imageFile.isEmpty()) {
            String uuString = UUID.randomUUID().toString();
            product.setImages(storageService.getSorageFilename(imageFile, uuString));
            storageService.store(imageFile, product.getImages());
        }

        productService.save(product);
        return new ResponseEntity<>(new Response(true, "Thêm Thành công", product), HttpStatus.OK);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(
            @Validated @RequestParam("productId") Long productId,
            @Validated @RequestParam("productName") String productName,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Validated @RequestParam("unitPrice") Double unitPrice,
            @Validated @RequestParam("discount") Double discount,
            @Validated @RequestParam("description") String description,
            @Validated @RequestParam("categoryId") Long categoryId,
            @Validated @RequestParam("quantity") Integer quantity,
            @Validated @RequestParam("status") Short status) {

        Optional<Product> opt = productService.findById(productId);
        if (opt.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Product", null), HttpStatus.BAD_REQUEST);
        }

        Product product = opt.get();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setQuantity(quantity);
        product.setStatus(status);

        Optional<Category> cateOpt = categoryService.findById(categoryId);
        if (cateOpt.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Category không tồn tại", null), HttpStatus.BAD_REQUEST);
        }
        product.setCategory(cateOpt.get());

        if (imageFile != null && !imageFile.isEmpty()) {
            String uuString = UUID.randomUUID().toString();
            product.setImages(storageService.getSorageFilename(imageFile, uuString));
            storageService.store(imageFile, product.getImages());
        }

        productService.save(product);
        return new ResponseEntity<>(new Response(true, "Cập nhật Thành công", product), HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@Validated @RequestParam("productId") Long productId) {
        Optional<Product> opt = productService.findById(productId);
        if (opt.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Không tìm thấy Product", null), HttpStatus.BAD_REQUEST);
        }
        productService.delete(opt.get());
        return new ResponseEntity<>(new Response(true, "Xóa Thành công", opt.get()), HttpStatus.OK);
    }
}
