package com.example.lostandfound.repository;

import com.example.lostandfound.entity.LostItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostItemRepository extends JpaRepository<LostItem, Long> {
    // Custom query methods can be added here
}
