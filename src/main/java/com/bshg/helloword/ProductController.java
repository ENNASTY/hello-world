package com.bshg.helloword;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final List<Product> productList = new ArrayList<>();
    private Long productIdCounter = 1L;

    @PostConstruct
    public void init() {
        productList.add(new Product(1L, "Product A", 10.0));
        productList.add(new Product(2L, "Product B", 20.0));
        productList.add(new Product(3L, "Product C", 30.0));
    }

    // Get all products
    @GetMapping
    public String getAllProducts(Model model) {
        model.addAttribute("products", productList);
        return "product-list";
    }

    // Show form for creating new product
    @GetMapping("/new")
    public String showCreateProductForm(Model model) {
        model.addAttribute("product", new Product(null, "", 0.0)); // Initialize new product
        return "product-form";
    }

    // Create a new product
    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product) {
        product.setId(productIdCounter++); // Set the new product ID
        productList.add(product); // Add the product to the list
        return "redirect:/products"; // Redirect to the product list page
    }

    // Show form for editing an existing product
    @GetMapping("/edit/{id}")
    public String showUpdateProductForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productList.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (product.isPresent()) {
            model.addAttribute("product", product.get()); // Pass the existing product for editing
        } else {
            // If product not found, redirect to product list page
            return "redirect:/products";
        }
        return "product-form";
    }

    // Update an existing product
    @PostMapping("/update")
    public String updateProduct(@ModelAttribute Product product) {
        Optional<Product> existingProduct = productList.stream()
                .filter(p -> p.getId().equals(product.getId()))
                .findFirst();

        if (existingProduct.isPresent()) {
            // Replace the existing product with the updated one
            existingProduct.get().setName(product.getName());
            existingProduct.get().setPrice(product.getPrice());
        }
        return "redirect:/products"; // Redirect to the product list page
    }

    // Delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productList.removeIf(p -> p.getId().equals(id)); // Remove the product by ID
        return "redirect:/products"; // Redirect to the product list page
    }
}
