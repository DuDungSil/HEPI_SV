package org.hepi.hepi_sv.model.vo;

import lombok.Data;

@Data
public class ShopProduct {
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
