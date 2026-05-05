package com.ecoscan.app.model;

import com.google.gson.annotations.SerializedName;

/**
 * A response structure could look like this:
 * <p>
 * {
 *   "product": {
 *     "product_name": "Coca-Cola",
 *     "brands": "Coca-Cola"
 *   }
 * }
 * ApiResponse class represents the whole response.
 * Inner class Product represents the product object INSIDE that response.
 * */

public class ApiResponse {
    private String message;

    @SerializedName("status")           // 1 = found, 0 = not found
    private int status;

    @SerializedName("product")          // Means map "product" inside the response
    public Product product;             // To this Java Object

    public String getMessage(){
        return message;
    }
    public int getStatus(){
        return status;
    }

    public static class Product {
        @SerializedName("product_name")     // means map "product_name" inside product object
        public String productName;          // to this java property

        @SerializedName("brands")           // means map "brands" inside product object
        public String brand;                // to this java property

        @SerializedName("quantity")
        public String quantity;

        @SerializedName("image_url")
        public String imageUrl;
    }
}
