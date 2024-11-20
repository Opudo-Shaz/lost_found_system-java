package com.example.lostandfound.controller;

import com.example.lostandfound.dtos.ClaimFoundItemDTO;
import com.example.lostandfound.dtos.FoundItemDTO;
import com.example.lostandfound.entity.FoundItem;
import com.example.lostandfound.service.EmailService;
import com.example.lostandfound.service.FileUploadService;
import com.example.lostandfound.service.FoundItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/found-items")
public class FoundItemController {
    private static final Logger logger = LoggerFactory.getLogger(FoundItemController.class);


    private final FoundItemService foundItemService;
    private final FileUploadService fileUploadService;
    private final EmailService emailService;

    // Constructor-based injection for required services
    @Autowired
    public FoundItemController(FoundItemService foundItemService, FileUploadService fileUploadService, EmailService emailService) {
        this.foundItemService = foundItemService;
        this.fileUploadService = fileUploadService;
        this.emailService = emailService;
    }

    // List of categories for the dropdown
    private static final List<String> CATEGORIES = Arrays.asList(
            "Personal Items", "Clothing & Accessories", "Electronics", "Books & Stationery",
            "Outdoor & Sports Equipment", "Travel & Luggage", "Toys & Games",
            "Pets & Pet Accessories", "Food & Drink", "Miscellaneous",
            "Furniture & Household Items", "Medical Supplies", "Musical Instruments & Equipment",
            "Holiday & Seasonal Items"
    );

    // Display the list of found items
    @GetMapping
    public String index(Model model) {
        logger.info("Fetching all found items.");
        List<FoundItem> foundItems = foundItemService.getAllFoundItems();
        model.addAttribute("items", foundItems);
        model.addAttribute("isRootUri", true);
        return "foundItems";
    }

    // Show form to post a new found item
    @GetMapping("/post-found-item")
    public String showPostFoundItemForm(Model model) {
        logger.info("Displaying form to post a new found item.");
        model.addAttribute("foundItem", new FoundItem());
        model.addAttribute("categories", CATEGORIES);
        model.addAttribute("isRootUri", true);
        return "addFoundItem"; // Thymeleaf template for posting new found item
    }

    // Submit a new-found item
    @PostMapping("/post-found-item")
    public String postFoundItem(@ModelAttribute FoundItem foundItem, RedirectAttributes redirectAttributes) {
        logger.info("Posting a new found item: {}", foundItem);
        try {
            foundItemService.saveFoundItem(foundItem);
            redirectAttributes.addFlashAttribute("success", "Found item posted successfully!");
        } catch (Exception e) {
            logger.error("Error posting found item.", e);
            redirectAttributes.addFlashAttribute("error", "Error posting found item.");
        }
        return "redirect:/found-items";
    }

    // View a specific found item by ID
    @GetMapping("/view-found-item/{id}")
    public String viewFoundItem(@PathVariable("id") Long id, Model model) {
        logger.info("Viewing found item with ID: {}", id);
        try {
            FoundItem foundItem = foundItemService.getFoundItemById(id);
            model.addAttribute("foundItem", foundItem);
            model.addAttribute("isRootUri", true);
            return "viewFoundItem"; // Display the specific found item
        } catch (Exception e) {
            logger.error("Found item with ID {} not found.", id, e);
            model.addAttribute("error", "Found item not found.");
            return "errorPage"; // Show error page if not found
        }
    }

    // Edit a specific found item by ID (Admin-only access)
    @Secured("ROLE_ADMIN")
    @GetMapping("/edit-found-item/{id}")
    public String editFoundItem(@PathVariable("id") Long id, Model model) {
        logger.info("Editing found item with ID: {}", id);
        try {
            FoundItem foundItem = foundItemService.getFoundItemById(id);
            model.addAttribute("foundItem", foundItem);
            model.addAttribute("categories", CATEGORIES);
            model.addAttribute("isRootUri", true);
            return "editFoundItem"; // Edit found item page
        } catch (Exception e) {
            logger.error("Error retrieving found item with ID {} for editing.", id, e);
            model.addAttribute("error", "Found item not found.");
            return "error";
        }
    }

    // Save the edited found item
    @PostMapping("/save-found-item")
    public String saveFoundItem(@ModelAttribute FoundItem foundItem, RedirectAttributes redirectAttributes) {
        logger.info("Saving found item: {}", foundItem);
        try {
            foundItemService.saveFoundItem(foundItem);
            redirectAttributes.addFlashAttribute("success", "Found item updated successfully!");
            logger.info("Found item updated successfully.");
        } catch (Exception e) {
            logger.error("Error saving found item.", e);
            redirectAttributes.addFlashAttribute("error", "Error saving found item.");
        }
        return "redirect:/found-items";
    }

    // Delete a specific found item by ID (Admin-only access)
    @Secured("ROLE_ADMIN")
    @GetMapping("/delete-found-item/{id}")
    public String deleteFoundItem(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            foundItemService.deleteFoundItem(id);
            redirectAttributes.addFlashAttribute("success", "Found item deleted successfully!");
            logger.info("Found item deleted successfully.");
        } catch (Exception e) {
            logger.error("Error deleting found item with ID {}.", id, e);
            redirectAttributes.addFlashAttribute("error", "Error deleting found item.");
        }
        return "redirect:/found-items";
    }

    // Show the form for claiming a found item
    @GetMapping("/claim/{id}")
    public String showClaimForm(@PathVariable("id") Long id, Model model) {
        logger.info("Showing claim form for found item ID: {}", id);
        ClaimFoundItemDTO claim = new ClaimFoundItemDTO();
        claim.setItemId(id);
        model.addAttribute("claim", claim);
        model.addAttribute("isRootUri", true);
        return "claimFoundItem"; // Claim form template
    }

    @PostMapping("/save-claim/{id}")
    public String saveClaim(@ModelAttribute("claim") ClaimFoundItemDTO claimDTO,
                            @RequestParam("image") MultipartFile image,
                            @RequestParam("receipt") MultipartFile receipt,
                            RedirectAttributes redirectAttributes,
                            @PathVariable Long id) {
        logger.info("Submitting claim for found item ID: {}", id);

        try {
            // Retrieve the FoundItemDTO by its ID
            FoundItemDTO foundItemDTO = foundItemService.getFoundItemDTO(id);
            String finderEmail = foundItemDTO.getFinderEmail();

            if (finderEmail == null || finderEmail.isEmpty()) {
                throw new IllegalArgumentException("Finder's email is missing or invalid.");
            }

            // Handle file uploads for image and receipt
            String imagePath = fileUploadService.uploadFile(image); // Upload image
            String receiptPath = fileUploadService.uploadFile(receipt); // Upload receipt

            // Prepare the claimer images list
            List<String> claimerImages = new ArrayList<>();
            claimerImages.add(imagePath);
            claimerImages.add(receiptPath); // Add receipt path to the images list

            // Update the found item, set status to PENDING, and set the claimedBy username
            foundItemService.claimFoundItem(id, claimDTO.getClaimerNote(), claimDTO.getClaimedBy(), claimerImages);

            // Prepare the claimer contact (assuming it's part of ClaimFoundItemDTO)
            String claimerContact = claimDTO.getClaimerContact(); // Assuming claimer contact is available in the DTO

            // Send email notification to the finder
            emailService.sendClaimNotificationEmail(
                    finderEmail,
                    claimDTO.getClaimedBy(),
                    foundItemDTO.getName(),
                    claimDTO.getClaimerContact(),
                    claimDTO.getClaimerNote(),
                    String.join(", ", claimerImages) // Convert the list of images to a comma-separated string
            );

            // Send email notification to the claimant
            emailService.sendClaimantNotificationEmail(
                    claimDTO.getClaimedBy(), // Assuming this is the claimant's email
                    foundItemDTO.getName(),
                    foundItemDTO.getFinderContact(), // Assuming the finder contact is part of FoundItemDTO
                    claimDTO.getLocationFound() // Assuming the locationFound is part of ClaimFoundItemDTO
            );

            // Build success message
            String successMessage = String.format(
                    "Claim submitted successfully! Reach out to the claimant to reunite with your item. " +
                            "The item '%s' was claimed by %s. The claimer's contact is %s, email is %s, " +
                            "and the finder is located at %s.",
                    foundItemDTO.getName(),
                    claimDTO.getClaimedBy(),
                    claimDTO.getClaimerContact(),
                    foundItemDTO.getFinderEmail(),
                    claimDTO.getLocationFound()
            );

            // Add a success message and redirect
            logger.info("Claim submitted successfully for item ID: {}", id);
            redirectAttributes.addFlashAttribute("success", successMessage);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error submitting claim for item ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Error submitting claim.");
        }
        return "redirect:/found-items";
    }


    // Method to search Found Items based on a query
    @GetMapping("/search")
    public String searchFoundItems(@RequestParam("query") String query, Model model) {
        List<FoundItem> foundItems = foundItemService.searchFoundItems(query);
        model.addAttribute("items", foundItems);
        model.addAttribute("query", query);
        logger.info("Search results for query '{}': {} items found.", query, foundItems.size());
        return "foundItems";
    }
}
