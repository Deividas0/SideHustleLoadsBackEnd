package com.example.demo.Utility;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

@Service
public class ImageBBService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "bd9cbec7860f5e1b5c0e9b3b5e11c259"; // Replace with your actual ImageBB API key
    public final String defaultImageUrl = "https://i.ibb.co/8M8yPnQ/no-image.jpg"; // Default image URL

    public String uploadImage(String imageInput) {
        if (imageInput == null || imageInput.isEmpty()) {
            return defaultImageUrl; // Return default image URL if no input is provided
        }

        try {
            String encodedImage;

            // Check if the input is Base64 or a file path
            if (isBase64(imageInput)) {
                // Input is already a Base64 string
                encodedImage = imageInput;
            } else {
                // Assume input is a file path, read the file and encode it to Base64
                byte[] fileContent = Files.readAllBytes(Paths.get(imageInput));
                encodedImage = Base64.getEncoder().encodeToString(fileContent);
            }

            // Prepare request body
            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("key", apiKey);  // Add API key
            requestBody.add("image", encodedImage);  // Add encoded image

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Send POST request
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange("https://api.imgbb.com/1/upload", HttpMethod.POST, requestEntity, Map.class);

            // Parse response
            if (response.getBody() != null && response.getBody().containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                return (String) data.get("url");  // Return uploaded image URL
            } else {
                throw new RuntimeException("Invalid response from ImageBB: " + response.getBody());
            }

        } catch (Exception e) {
            System.err.println("Error uploading image to ImageBB: " + e.getMessage());
            throw new RuntimeException("Error uploading image to ImageBB: " + e.getMessage());
        }
    }

    // Helper method to check if the input is Base64 encoded
    private boolean isBase64(String input) {
        // Basic validation for Base64: must only contain valid Base64 characters
        String base64Regex = "^[A-Za-z0-9+/=]+$";
        if (!input.matches(base64Regex)) {
            return false;
        }

        // Additional validation: check if input length is a multiple of 4
        return input.length() % 4 == 0;
    }

    public String defaultImageUrl() {
        return defaultImageUrl;
    }
}
