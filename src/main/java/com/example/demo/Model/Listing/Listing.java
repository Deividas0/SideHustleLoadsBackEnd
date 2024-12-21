package com.example.demo.Model.Listing;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Listing {

    private int id;
    private int created_by_userid;
    private String title;
    private String description;
    private String pickUpLocation;
    private String deliveryLocation;
    private String mustDeliverBefore;

}
