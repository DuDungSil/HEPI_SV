package org.hepi.hepi_sv.model.vo;

import lombok.Data;

@Data
public class Gym {
    private String id;
    private String name;

    public Gym(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ID : " + this.id
                + ", NAME : " + this.name;
    }
}
