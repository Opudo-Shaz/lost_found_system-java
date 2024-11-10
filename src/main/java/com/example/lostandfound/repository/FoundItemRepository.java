package com.example.lostandfound.repository;

import com.example.lostandfound.entity.FoundItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
    // Custom query methods can be added here
}
