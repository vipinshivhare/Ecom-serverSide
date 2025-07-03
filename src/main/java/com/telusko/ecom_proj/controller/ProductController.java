package com.telusko.ecom_proj.controller;

import java.io.IOException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.telusko.ecom_proj.model.Product;
import com.telusko.ecom_proj.service.ProductService;

@RestController
@CrossOrigin 
@RequestMapping("/api")
public class ProductController {
	
	@Autowired 
	private ProductService service;
	
	
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts(){
		return new ResponseEntity<> (service.getAllProducts(),HttpStatus.OK); 
	}
	
	@GetMapping("/product/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable int id) {
		Product product = service.getProductById(id);
		if(product != null)
			return new ResponseEntity<> (product,HttpStatus.OK); 
		else 
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
	}
	
	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestPart("product") Product product,
	                                    @RequestPart("imageFile") MultipartFile imageFile) {
	    try {
	    	 System.out.println("Available: " + product.isAvailable());
	         System.out.println("Quantity: " + product.getQuantity());
	         System.out.println("Release Date: " + product.getReleaseDate());
	         
	        Product product1 = service.addProduct(product, imageFile);
	        return new ResponseEntity<>(product1, HttpStatus.CREATED);
	    } catch (Exception e) {
	        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	@PutMapping("/product/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable int id,
	                                            @RequestPart("product") Product product, // Explicitly name the part "product"
	                                            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile) throws IOException { // Made imageFile optional
	    try {
	        Product updatedProduct = service.updateProduct(id, product, imageFile);
	        if (updatedProduct != null) {
	            return new ResponseEntity<>("Product updated successfully", HttpStatus.OK);
	        } else {
	            // This case might mean product with ID was not found in service layer
	            return new ResponseEntity<>("Product not found or failed to update", HttpStatus.NOT_FOUND);
	        }
	    } catch (Exception e) { // Catch more general exceptions for robustness
	        // Log the exception for debugging purposes
	        e.printStackTrace(); // Consider using a logger like SLF4J/Logback in production
	        return new ResponseEntity<>("Error updating product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id){
		Product product = service.getProductById(id);
		if(product != null) {
			service.deleteProduct(id);
			return new ResponseEntity<>("Deleted",HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Product not found",HttpStatus.NOT_FOUND);
		}
	}
	 
	@GetMapping("/products/search")
	public ResponseEntity<List<Product>> searchProducts(String keyword){
		System.out.println("Searching with " + keyword);
		List<Product> products = service.searchProducts(keyword);
		return new ResponseEntity<>(products, HttpStatus.OK); 
	} 
}

