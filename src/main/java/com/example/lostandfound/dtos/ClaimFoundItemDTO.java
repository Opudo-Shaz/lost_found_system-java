package com.example.lostandfound.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ClaimFoundItemDTO {
    private Long itemId;
    private String claimedBy;
    private String claimerContact;
    private String locationFound;
    private String claimerNote;
    private List<String> claimerImages;

    // Constructor
    public ClaimFoundItemDTO() {}

    public ClaimFoundItemDTO(Long itemId, String claimedBy,String claimerContact, String locationFound ,String claimerNote, List<String> claimerImages) {
        this.itemId = itemId;
        this.claimedBy = claimedBy;
        this.claimerNote = claimerNote;
        this.claimerImages = claimerImages;
        this.claimerContact = claimerContact;
        this.locationFound = locationFound;
    }

}

