package com.example.lostandfound.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FoundItemClaim {

    private Long itemId;  // Assuming itemId is of type Long
    private String note;  // For the ownership note
    private MultipartFile image;  // For the image of the lost item
    private MultipartFile receipt;  // For the receipt (optional)


    public void setImagePath(String imagePath) {
    }

    public void setReceiptPath(String receiptPath) {
    }
}
