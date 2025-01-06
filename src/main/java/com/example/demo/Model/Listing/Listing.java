package com.example.demo.Model.Listing;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Listing {

    private int id;
    private int createdByUserid;
    private String title;
    private String description;
    private String pickUpCountry;
    private String deliveryCountry;
    private String pickUpLocation;
    private String deliveryLocation;
    private String mustDeliverBefore;
    private String weight;
    private String height;
    private String width;
    private String loadType;
    private String base64Image;
    private String createdAt;

    public Listing(int createdByUserid, String title, String description, String pickUpLocation, String deliveryLocation) {
        this.createdByUserid = createdByUserid;
        this.title = title;
        this.description = description;
        this.pickUpLocation = pickUpLocation;
        this.deliveryLocation = deliveryLocation;
    }
}
