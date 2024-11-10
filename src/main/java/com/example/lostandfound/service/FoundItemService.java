package com.example.lostandfound.service;

import com.example.lostandfound.entity.FoundItem;
import com.example.lostandfound.repository.FoundItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoundItemService {

    private final FoundItemRepository foundItemRepository;

    // Constructor-based injection
    public FoundItemService(FoundItemRepository foundItemRepository) {
        this.foundItemRepository = foundItemRepository;
    }

    // Get all found items
    public List<FoundItem> getAllFoundItems() {
        return foundItemRepository.findAll();
    }

    // Save or update a found item
    public void saveFoundItem(FoundItem foundItem) {
        foundItemRepository.save(foundItem);
    }

    // Get a found item by ID
    public FoundItem getFoundItemById(Long id) {
        Optional<FoundItem> foundItemOptional = foundItemRepository.findById(id);
        return foundItemOptional.orElse(null); // Return the item if present, otherwise null
    }

    // Delete a found item by ID
    public void deleteFoundItem(Long id) {
        foundItemRepository.deleteById(id);
    }
    // Method to claim a found item (update the claimed status)
    public void claimFoundItem(Long itemId) {
        // Find the found item by ID
        FoundItem foundItem = foundItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        // Set the claimed status to true
        foundItem.setClaimed(true);

        // Save the updated found item to the database
        foundItemRepository.save(foundItem);
    }
    // Additional methods can be added as needed
}
