package com.example.demo.Controller;

import com.example.demo.Model.Listing.Listing;
import com.example.demo.Service.ListingService;
import com.example.demo.Utility.JwtToken.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/listing")
public class ListingController {

    @Autowired
    private ListingService listingService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @PostMapping("/new")
    public ResponseEntity<?> newListing(@RequestHeader("Authorization") String authorizationHeader,
                                        @RequestBody Listing listing) throws SQLException {
        int id = jwtDecoder.decodeUserIdFromToken(authorizationHeader);
        listing.setCreatedByUserid(id);
        listingService.newListing(listing);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
