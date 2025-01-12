# SideHustleLoads

## Introduction
**SideHustleLoads** is a Europe-based platform dedicated to delivery services. It connects individuals who need items delivered with those willing to deliver them. Whether you're sending packages, moving goods, or looking for delivery opportunities, SideHustleLoads provides an easy-to-use platform to facilitate these transactions.

## Features
- **List Deliveries**: Users can post items they need delivered, including package details, pickup and drop-off locations, and preferred timeframes.
- **Browse Listings**: Search for available delivery jobs based on location, type, or schedule.

---

## Installation Instructions

### Prerequisites
1. **Java Development Kit (JDK)**: Version 11 or higher.
2. **IntelliJ IDEA**: Community or Ultimate Edition for back-end development.
3. **MySQL Server**: For database management.
4. **Visual Studio Code**: For front-end development.
5. **Postman or ThunderClient**: For API testing.


### Steps to Set Up Locally

### Step 1: Launch IntelliJ IDEA
1. Open IntelliJ IDEA.
2. On the **Welcome Screen**, select **Get from VCS**.
    - If you are already working on a project, click **File > New > Project from Version Control**.

---

### Step 2: Enter GitHub Repository Details
1. In the **Get from Version Control** window:
    - **Version Control**: Select **Git**.
    - **URL**: Enter your GitHub repository link. For this project:
      ```plaintext
      https://github.com/Deividas0/SideHustleLoadsBackEnd
      ```
      ### Also Front-end repository for this project
    - ```plaintext
      https://github.com/Deividas0/SideHustleLoadsFront
      ```
    - **Directory**: Choose or create a folder where the project will be cloned on your local machine.
2. Click **Clone**.

---

### Step 3: Authenticate with GitHub
1. If prompted, log in to GitHub:
   - Click **Log In to GitHub** and follow the instructions.
   - Or authenticate using a **Personal Access Token**:
      1. Go to **GitHub > Settings > Developer Settings > Personal Access Tokens**.
      2. Generate a token with **repo** and **read:user** permissions.
      3. Paste the token into IntelliJ when prompted.

---

### Step 4: Open and Configure the Project
1. Once cloning is complete, IntelliJ will ask if you want to open the project. Select **Yes**.
2. If IntelliJ doesn’t detect the project type:
   - Navigate to **File > Project Structure**.
   - Set the correct **JDK version** (e.g., JDK 11 or higher).
   - Click **OK** to save.

---

### Step 5: Build and Run the Project
1. Open the **Run/Debug Configurations** in IntelliJ:
   - Go to **Run > Edit Configurations**.
   - Add a new configuration for your project if one isn't already present.
2. Run the project:
   - Click on the green play button in the top-right corner.
   - Alternatively, use **Run > Run '<Your Project Name>'** from the menu.

---

### Step 6: Sync the Project with GitHub (Optional)
1. To fetch changes from the repository:
   - Go to **VCS > Git > Pull**.
2. To push your changes to the repository:
   - Commit your changes in IntelliJ.
   - Go to **VCS > Git > Push** to upload your changes.

---

## Additional Notes
- Use IntelliJ IDEA's **Git Tool Window** for easier branch management, commits, and merges.
- Ensure you keep IntelliJ IDEA updated to avoid compatibility issues with Git or other tools.

---

You are now ready to work on your project using IntelliJ IDEA!

### Database Tables

#### `user` Table
- **Description**: Stores user information.
- **Columns**:
   - `id`: Primary key, auto-incrementing integer.
   - `username`: Unique string.
   - `email`: Unique string.
   - `country`: Unique string.
   - `whatsapp`: Unique string.
   - `viber`: Unique string.
   - `password`: Hashed string.
   - `registration_date`: Timestamp, default `CURRENT_TIMESTAMP`.
   - `total_listing_created`: Integer.
   - `balance`: Integer.
   - `status`: String.

#### `listing` Table
- **Description**: Stores delivery request details.
- **Columns**:
   - `id`: Primary key, auto-incrementing integer.
   - `created_by_userid`: Foreign key referencing `user(id)`.
   - `title`: Title of the listing.
   - `description`: Text describing the delivery job.
   - `pick_up_country`: Text describing pick-up country of the package.
   - `delivery_country`: Text describing drop-off country of the package.
   - `pick_up_location`: String, pickup address.
   - `delivery_location`: String, drop-off address.
   - `must_deliver_before`: Optional string.
   - `weight`: Optional string, default NULL.
   - `height`: Optional string, default NULL.
   - `width`: Optional string, default NULL.
   - `load_type`: Optional string, default NULL.
   - `image_url`: Optional string, default NULL.
   - `creation_date`: Timestamp, default `CURRENT_TIMESTAMP`.


### API Endpoints UserController
```java
 @RequestMapping("/user")
```

```java
@PostMapping("/registration")
public ResponseEntity<String> registration(@RequestBody UserRegistrationDTO user)

@PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDTO userLoginDTO)

@GetMapping("/profile")
    public UserProfileDTO getUserProfileDTO(@RequestHeader("Authorization") String authorizationHeader)

@PutMapping("/profile")
    public ResponseEntity<Void> updateUserProfile(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserProfileDTO updatedProfile)

@GetMapping("/balance")
    public ResponseEntity<Map<String, Object>> getUserBalance(
            @RequestHeader("Authorization") String authorizationHeader)

@PostMapping("/vip")
    public ResponseEntity<Map<String, Object>> purchaseVip(
            @RequestBody Map<String, Integer> request,
            @RequestHeader("Authorization") String authorizationHeader)

```

### API Endpoints ListingController
```java
 @RequestMapping("/listing")
```

```java
@PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> newListing(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestPart("listing") String listingJson,
            @RequestPart(value = "file", required = false) MultipartFile file)

@GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getListingById(
            @PathVariable int id,
            @RequestHeader("Authorization") String authorizationHeader)

@GetMapping("/byuserid")
    public ResponseEntity<List<Listing>> getAllListingsByUserId(@RequestHeader("Authorization") String authorizationHeader)

@PostMapping("/filter")
    public ResponseEntity<List<Listing>> getFilteredListings(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, String> filters)

@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteListingById(
            @PathVariable int id,
            @RequestHeader("Authorization") String authorizationHeader)

@PutMapping("/{id}")
    public ResponseEntity<String> updateListing(
            @PathVariable int id,
            @RequestBody Listing updatedListing,
            @RequestHeader("Authorization") String authorizationHeader)
```

### API Endpoints PaymentController
```java
 @RequestMapping("/api/payment")
```
```java
@PostMapping("/create-stripe-session")
    public ResponseEntity<Map<String, String>> createStripeSession(
            @RequestBody Map<String, String> request,
            @RequestHeader("Authorization") String authorizationHeader)

@PostMapping("/update-balance")
    public ResponseEntity<Map<String, Object>> updateBalance(
            @RequestBody Map<String, String> request)
```

### Front-End System architecture

**You can see how everything is connected in the picture**
- https://ibb.co/Zdmq143
### Front-End files

#### `html` files
- **index.html** - Main page.
- **registration.html** - Registration page.
- **login.html** - Login page.
- **ads.html** - Ads page.
- **cancel.html** - Canceled payment page.
- **modifyad.html** - Ad modifying page.
- **myads.html** - Page to manage your ads.
- **myprofile.html** - Page to manage your profile.
- **newad.html** - Page to add new ad.
- **shop.html** - Inside shop page.
- **single-ad.html** - Display single selected ad.
- **success.html** - Successful payment page.
- **topup.html** - Page to top-up balance.

#### `js` files located in `/js` folder.
- **index.js**
- **registration.js**
- **login.js**
- **ads.js**
- **cancel.js**
- **modifyad.js**
- **myads.js**
- **myprofile.js**
- **newad.js**
- **shop.js**
- **single-ad.js**
- **success.js**
- **topup.js**

#### `css` files located in `/css` folder.
- **index.css**
- **registration.css**
- **login.css**
- **ads.css**
- **cancel.css**
- **modifyad.css**
- **myads.css**
- **myprofile.css**
- **newad.css**
- **shop.css**
- **single-ad.css**
- **success.css**
- **topup.css**

#### files located in `/foto` folder.
- **indexbackground.webp**

### Back-end model classes

#### `User` class
- **private int id;**
- **private String username;**
- **private String country;**
- **private String whatsapp;**
- **private String viber;**
- **private String email;**
- **private String password;**
- **private String registrationDate;**
- **private int totalListingsCreated;**

#### `UserLoginDTO` class
- **private String username;**
- **private String password;**

#### `UserProfileDTO` class
- **private String username;**
- **private String country;**
- **private String whatsapp;**
- **private String viber;**
- **private String registrationDate;**
- **private int totalListingsCreated;**
- **private int balance;**
- **private String status;**

#### `UserRegistrationDTO` class
- **private String username;**
- **private String email;**
- **private String password;**
- **private String country;**

#### `Listing` class
- **private int id;**
- **private int createdByUserid;**
- **private String title;**
- **private String description;**
- **private String pickUpCountry;**
- **private String deliveryCountry;**
- **private String pickUpLocation;**
- **private String deliveryLocation;**
- **private String mustDeliverBefore;**
- **private String weight;**
- **private String height;**
- **private String width;**
- **private String loadType;**
- **private String base64Image;**
- **private String createdAt;**

