package com.example.demo.Controller;

import com.example.demo.Model.Listing.Listing;
import com.example.demo.Model.User.UserProfileDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getListingById(
            @PathVariable int id,
            @RequestHeader("Authorization") String authorizationHeader) throws SQLException {

        if (authorizationHeader == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Listing listing = listingService.getListingById(id);
        if (listing != null) {
            UserProfileDTO userProfileDTO = userService.getUserProfileById(listing.getCreatedByUserid());
            Map<String, Object> response = new HashMap<>();
            response.put("listing", listing);
            response.put("userProfileDTO", userProfileDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
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

    @PostMapping("/filter")
    public ResponseEntity<List<Listing>> getFilteredListings(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, String> filters
    ) throws SQLException {
        if (authorizationHeader == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String fromCountry = filters.get("pickUpCountry");
        String toCountry = filters.get("deliveryCountry");

        List<Listing> listings = listingService.getFilteredListings(fromCountry, toCountry);
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


        if (userid != createdById) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        boolean isDeleted = listingService.deleteListingById(id);

        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Success?
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateListing(
            @PathVariable int id,
            @RequestBody Listing updatedListing,
            @RequestHeader("Authorization") String authorizationHeader) {

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return new ResponseEntity<>("Unauthorized access. Missing token.", HttpStatus.UNAUTHORIZED);
        }

        if (updatedListing.getId() != id) {
            return new ResponseEntity<>("Listing ID mismatch.", HttpStatus.BAD_REQUEST);
        }

        try {
            boolean isUpdated = listingService.updateListing(updatedListing);

            if (isUpdated) {
                return new ResponseEntity<>("Listing updated successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Listing not found or not updated.", HttpStatus.NOT_FOUND);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>("Internal server error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
