package org.hepi.hepi_sv.vo;

import lombok.Data;

@Data
public class Product {
    private int id;
    private String name;
    private int price;
    private String explain;
    private int event;
    private String main_image;
    private int amount;
    private String unit;
}
