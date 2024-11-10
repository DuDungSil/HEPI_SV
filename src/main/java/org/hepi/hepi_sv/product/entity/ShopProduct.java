package org.hepi.hepi_sv.product.entity;

import lombok.Data;

@Data
public class ShopProduct {
    int product_id;
    String name;
    String url;
    String img_url;
    String brand;
    int price;
    int sale_price;
    int discount;
    double rating;
    int review;
    String category;
    String keyword;
    Boolean is_rocket_delivery;
    Boolean is_rocket_fresh;
}
