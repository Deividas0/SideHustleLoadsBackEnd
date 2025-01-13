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
    private final String apiKey = "bd9cbec7860f5e1b5c0e9b3b5e11c259";
    public final String defaultImageUrl = "https://i.ibb.co/8M8yPnQ/no-image.jpg";

    public String uploadImage(String imageInput) {
        if (imageInput == null || imageInput.isEmpty()) {
            return defaultImageUrl;
        }

        try {
            String encodedImage;

            if (isBase64(imageInput)) {
                encodedImage = imageInput;
            } else {
                byte[] fileContent = Files.readAllBytes(Paths.get(imageInput));
                encodedImage = Base64.getEncoder().encodeToString(fileContent);
            }

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("key", apiKey);
            requestBody.add("image", encodedImage);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange("https://api.imgbb.com/1/upload", HttpMethod.POST, requestEntity, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                return (String) data.get("url");
            } else {
                throw new RuntimeException("Invalid response from ImageBB: " + response.getBody());
            }

        } catch (Exception e) {
            System.err.println("Error uploading image to ImageBB: " + e.getMessage());
            throw new RuntimeException("Error uploading image to ImageBB: " + e.getMessage());
        }
    }

    private boolean isBase64(String input) {
        String base64Regex = "^[A-Za-z0-9+/=]+$";
        if (!input.matches(base64Regex)) {
            return false;
        }

        return input.length() % 4 == 0;
    }

    public String defaultImageUrl() {
        return defaultImageUrl;
    }
}
