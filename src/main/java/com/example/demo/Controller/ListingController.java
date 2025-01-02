package com.example.demo.Controller;

import com.example.demo.Model.Listing.Listing;
import com.example.demo.Service.ListingService;
import com.example.demo.Service.UserService;
import com.example.demo.Utility.ImageBBService;
import com.example.demo.Utility.JwtToken.JwtDecoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@RequestMapping("/listing")
public class ListingController {

    @Autowired
    private ListingService listingService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private ImageBBService imageBBService;

    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> newListing(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestPart("listing") String listingJson,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException, SQLException {

        String imageUrl;
        if (file != null && !file.isEmpty()) {
            String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
            imageUrl = imageBBService.uploadImage(base64Image);
        } else {
            imageUrl = imageBBService.defaultImageUrl();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Listing listing = objectMapper.readValue(listingJson, Listing.class);

        int userId = jwtDecoder.decodeUserIdFromToken(authorizationHeader);

        listing.setBase64Image(imageUrl);
        listing.setCreatedByUserid(userId);

        listingService.newListing(listing);

        userService.totalListingsCreatedIncrement(userId);

        return ResponseEntity.ok("Listing created successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Listing> getListingById(
            @PathVariable int id,
            @RequestHeader("Authorization") String authorizationHeader) throws SQLException {

        if (authorizationHeader == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Listing listing = listingService.getListingById(id);
        if (listing != null) {
            return new ResponseEntity<>(listing, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byuserid")
    public ResponseEntity<List<Listing>> getAllListingsByUserId(@RequestHeader("Authorization") String authorizationHeader) throws SQLException {
        int id = jwtDecoder.decodeUserIdFromToken(authorizationHeader);
        List<Listing> listings = listingService.getAllListingsByUserId(id);
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Listing>> getAllListings(@RequestHeader("Authorization") String authorizationHeader) throws SQLException {
        if (authorizationHeader == null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        List<Listing> listings = listingService.getAllListings();
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListingById(
            @PathVariable int id,
            @RequestHeader("Authorization") String authorizationHeader) throws SQLException {

        int userid = jwtDecoder.decodeUserIdFromToken(authorizationHeader);
        Listing listing = listingService.getListingById(id);

        if (listing == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        int createdById = listing.getCreatedByUserid();


        if (userid != createdById){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean isDeleted = listingService.deleteListingById(id);

        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Success?
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




}
