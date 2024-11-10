package com.example.lostandfound.controller;

import com.example.lostandfound.entity.FoundItem;
import com.example.lostandfound.service.FileUploadService;
import com.example.lostandfound.entity.FoundItemClaim;
import com.example.lostandfound.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/found-items")
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    // List of categories for the dropdown
    private static final List<String> CATEGORIES = Arrays.asList(
            "Personal Items", "Clothing & Accessories", "Electronics", "Books & Stationery",
            "Outdoor & Sports Equipment", "Travel & Luggage", "Toys & Games",
            "Pets & Pet Accessories", "Food & Drink", "Miscellaneous",
            "Furniture & Household Items", "Medical Supplies", "Musical Instruments & Equipment",
            "Holiday & Seasonal Items"
    );

    //Display the list of found items
    @GetMapping
    public String index(Model model) {
        List<FoundItem> foundItems = foundItemService.getAllFoundItems();
        //System.out.println(foundItems.toString());
        model.addAttribute("items", foundItems);
        return "foundItems";
    }
    @GetMapping("/post-found-item")
    public String showPostFoundItemForm(Model model) {
        model.addAttribute("foundItem", new FoundItem());
        model.addAttribute("categories", CATEGORIES);  // Pass the categories list to the view
        return "addFoundItem"; // Thymeleaf template
    }

    // Submit a new-found item
    @PostMapping ("/post-found-item")
    public String postFoundItem(@ModelAttribute FoundItem foundItem) {
        foundItemService.saveFoundItem(foundItem);
        return "redirect:/found-items"; // Redirect back to the list of found items
    }

    //View a specific found item by ID
    @GetMapping("/view-found-item/{id}")
    public String viewFoundItem(@PathVariable("id") Long id, Model model) {
        FoundItem foundItem = foundItemService.getFoundItemById(id);
        model.addAttribute("foundItem", foundItem);
        return "viewFoundItem"; // The view template to display the specific found item
    }

    //Edit a specific found item by ID
    @GetMapping("/edit-found-item/{id}")
    public String editFoundItem(@PathVariable("id") Long id, Model model) {
        FoundItem foundItem = foundItemService.getFoundItemById(id);
        model.addAttribute("foundItem", foundItem);
        return "editFoundItem"; // The view template for editing a found item
    }

    // Save the edited found item
    @PostMapping("/save-found-item")
    public String saveFoundItem(@ModelAttribute FoundItem foundItem) {
        foundItemService.saveFoundItem(foundItem); // Save the updated item
        return "redirect:/found-items"; // Redirect to the list of found items
    }

    // Delete a specific found item by ID
    @GetMapping("/delete-found-item/{id}")
    public String deleteFoundItem(@PathVariable("id") Long id) {
        foundItemService.deleteFoundItem(id); // Delete the item by ID
        return "redirect:/found-items"; // Redirect to the list after deletion
    }


    @Autowired
    private FileUploadService fileUploadService;
    // Show the form for claiming a found item (evidence upload)
    @GetMapping("/claim/{id}")
    public String showClaimForm(@PathVariable("id") Long id, Model model) {
        FoundItemClaim claim = new FoundItemClaim();
        claim.setItemId(id);  // Set the found item ID
        model.addAttribute("claim");
        return "claimFoundItem";  // The form where the user will provide evidence
    }

    // Handle the claim submission and update the item status
    @PostMapping("/save-claim")
    public String saveClaim(@ModelAttribute FoundItemClaim claim,
                            @RequestParam("image") MultipartFile image,
                            @RequestParam("receipt") MultipartFile receipt) {
        try {
            // Handle the image and receipt upload
            String imagePath = fileUploadService.uploadFile(image);
            String receiptPath = fileUploadService.uploadFile(receipt);

            // Save the evidence paths
            claim.setImagePath(imagePath);
            claim.setReceiptPath(receiptPath);

            // Now, claim the item by updating the found item status
            foundItemService.claimFoundItem(claim.getItemId());

            return "redirect:/found-items";  // Redirect to the list of found items or a success page
        } catch (Exception e) {
            e.printStackTrace();
            return "error";  // Handle errors during claim submission
        }
    }
}

