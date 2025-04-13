# Lala E-Commerce - README

## Table of Contents
1. [Project Overview](#project-overview)
2. [Key Features](#key-features)
3. [Technologies Used](#technologies-used)
4. [Setup Instructions](#setup-instructions)
5. [Database Schema](#database-schema)
6. [How to Run the App](#how-to-run-the-app)


---

## Project Overview

**Lala E-Commerce** is a mobile application designed to facilitate online shopping for clothing. The app provides a seamless shopping experience for customers while enabling administrators to manage products efficiently. It includes essential features such as user registration, product browsing, cart management, payment processing, and administrative tools for product management.

This project was developed as part of the **Mobile Device Programming 1** course at Limkokwing University of Creative Technology.

---

## Key Features

### Customer Features:
- **User Registration & Login**: Customers can create accounts and log in securely.
- **Product Browsing**: Browse through a catalog of clothing items with filters and categories.
- **Shopping Cart**: Add or remove items from the cart and manage quantities.
- **Order Placement**: Complete purchases by generating invoices and processing payments.

### Administrator Features:
- **Product Management**: Create, modify, view, and delete product records.
- **Inventory Monitoring**: Keep track of stock levels and update product availability.
- **Sales Reports**: Generate reports on sales and customer activity.

---

## Technologies Used

- **Programming Language**: Java
- **Development Environment**: Android Studio IDE
- **Database**: SQLite (for local data storage)
- **Version Control**: Git and GitHub

---

## Setup Instructions

### Prerequisites
Before running the app, ensure you have the following installed:
- **Java Development Kit (JDK)**: Version 11 or higher.
- **Android Studio**: Latest stable version.
- **Git**: For cloning the repository.
- **SQLite Browser**: Optional, for viewing the database.

### Steps to Set Up the Project
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Kanytha/E-Commerce_App.git
   cd lala-ecommerce
   ```

2. **Open the Project in Android Studio**:
   - Launch Android Studio.
   - Select "Open an Existing Project" and navigate to the cloned repository folder.

3. **Set Up the Database**:
   - The app uses SQLite for data storage. The database schema is included in the `assets` folder.
   - Ensure the database file (`lala_ecommerce.db`) is correctly linked in the project.

4. **Install Dependencies**:
   - Android Studio will automatically sync Gradle dependencies. Ensure all required libraries are downloaded.

5. **Run the App**:
   - Connect an Android device or use an emulator.
   - Click the "Run" button in Android Studio to build and deploy the app.


## How to Run the App

1. **Launch the App**:
   - Upon opening the app, users will see a login screen. New users can register by clicking the "Sign Up" button.

2. **Customer Workflow**:
   - Log in as a customer.
   - Browse products using the navigation menu.
   - Add items to the cart and proceed to checkout.
   - Generate an invoice after completing the payment process.

3. **Admin Workflow**:
   - Log in as an admin (credentials provided in the documentation).
   - Navigate to the admin dashboard to manage products.
   - Use the CRUD operations to add, edit, or delete products.

---

