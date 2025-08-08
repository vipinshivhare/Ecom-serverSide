// package com.telusko.ecom_proj.service;

// import java.io.IOException;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional; // Import Optional

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

// import com.cloudinary.Cloudinary;
// import com.cloudinary.utils.ObjectUtils;
// import com.telusko.ecom_proj.model.Product;
// import com.telusko.ecom_proj.repo.ProductRepo;

// @Service
// public class ProductService {

// 	@Autowired
// 	private ProductRepo repo;

// 	@Autowired
// 	private Cloudinary cloudinary; 

// 	public List<Product> getAllProducts() {
// 		return repo.findAll();
// 	}

// 	public Product getProductById(int id) {
// 		return repo.findById(id).orElse(null);
// 	}

// 	public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
// 	    if (imageFile == null || imageFile.isEmpty()) {
// 	        throw new IllegalArgumentException("Image file is required for adding a new product.");
// 	    }

// 	    Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
// 	    String imageUrl = (String) uploadResult.get("secure_url");
// 	    product.setImageUrl(imageUrl);

// 	    return repo.save(product); 
// 	}

// 	public Product updateProduct(int id, Product updatedProductDetails, MultipartFile imageFile) throws IOException {
// 	    // 1. Find the existing product in the database
// 	    Optional<Product> existingProductOptional = repo.findById(id);

// 	    if (existingProductOptional.isEmpty()) {
// 	        // Product with the given ID was not found
// 	        return null;
// 	    }

// 	    Product existingProduct = existingProductOptional.get();

// 	    // 2. Update the fields of the existing product with the new details
// 	    // This ensures that other fields not sent by the frontend (like during checkout) are preserved.
// 	    existingProduct.setName(updatedProductDetails.getName());
// 	    existingProduct.setDescription(updatedProductDetails.getDescription());
// 	    existingProduct.setBrand(updatedProductDetails.getBrand());
// 	    existingProduct.setPrice(updatedProductDetails.getPrice());
// 	    existingProduct.setCategory(updatedProductDetails.getCategory());
// 	    existingProduct.setReleaseDate(updatedProductDetails.getReleaseDate());
// 	    existingProduct.setAvailable(updatedProductDetails.isAvailable());
// 	    existingProduct.setQuantity(updatedProductDetails.getQuantity());

// 	    // 3. Conditionally handle image file upload
// 	    // Only upload to Cloudinary IF a new imageFile is provided AND it's not empty.
// 	    // This is crucial for fixing the "NullPointerException" when no new image is sent (e.g., during checkout).
// 	    if (imageFile != null && !imageFile.isEmpty()) {
// 	        Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
// 	        existingProduct.setImageUrl((String) uploadResult.get("secure_url")); // Update imageUrl if new image is uploaded
// 	    }
// 	    // If imageFile is null or empty, the existingProduct's imageUrl remains unchanged,
// 	    // preserving the old image link.

// 	    // 4. Save the updated product back to the database
// 	    return repo.save(existingProduct);
// 	}

// 	public void deleteProduct(int id) {
// 		repo.deleteById(id);
// 	}

// 	public List<Product> searchProducts(String keyword) {
// 		return repo.searchProducts(keyword);
// 	}
// }


package com.vipin.ecom_proj.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vipin.ecom_proj.model.Product;
import com.vipin.ecom_proj.repo.ProductRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo repo;

    @Autowired
    private Cloudinary cloudinary;

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is required for adding a new product.");
        }

        String imageUrl = uploadImageToCloudinary(imageFile);
        product.setImageUrl(imageUrl);

        return repo.save(product);
    }

    public Product updateProduct(int id, Product updatedProductDetails, MultipartFile imageFile) throws IOException {
        Optional<Product> existingProductOptional = repo.findById(id);

        if (existingProductOptional.isEmpty()) {
            return null;
        }

        Product existingProduct = existingProductOptional.get();

        // Update fields
        existingProduct.setName(updatedProductDetails.getName());
        existingProduct.setDescription(updatedProductDetails.getDescription());
        existingProduct.setBrand(updatedProductDetails.getBrand());
        existingProduct.setPrice(updatedProductDetails.getPrice());
        existingProduct.setCategory(updatedProductDetails.getCategory());
        existingProduct.setReleaseDate(updatedProductDetails.getReleaseDate());
        existingProduct.setAvailable(updatedProductDetails.isAvailable());
        existingProduct.setQuantity(updatedProductDetails.getQuantity());

        // Update image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = uploadImageToCloudinary(imageFile);
            existingProduct.setImageUrl(imageUrl);
        }

        return repo.save(existingProduct);
    }

    public void deleteProduct(int id) {
        repo.deleteById(id);
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }

    // === PRIVATE HELPER METHOD ===
    private String uploadImageToCloudinary(MultipartFile file) throws IOException {
        Map<?, ?> uploadResult = cloudinary.uploader().upload(
            file.getBytes(),
            ObjectUtils.asMap(
                "folder", "ecom", // Save all images in "ecom" folder
				 "resource_type", "auto"
            )
        );
        return (String) uploadResult.get("secure_url");
    }
}
