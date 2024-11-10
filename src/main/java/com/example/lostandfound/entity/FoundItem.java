package com.example.lostandfound.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("FOUND") // Optional: specify a discriminator value for clarity
@Table(name = "found_items")
public class FoundItem extends Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime foundDate;
    private String Item_holder_name;

    private String locationFound;
    private String finderContact;
    private String dateFound;
    private Boolean isClaimed = false;
    private String claimed_by;
    private String note;
    private String imagePath;
    private String receiptPath;


    public boolean isClaimed() {
        return isClaimed;
    }

    public void setClaimed(boolean claimed) {
        this.isClaimed = claimed;
    }

}