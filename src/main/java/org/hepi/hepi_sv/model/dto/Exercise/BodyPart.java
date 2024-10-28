package org.hepi.hepi_sv.model.dto.Exercise;

import java.util.List;

import lombok.Data;

@Data
public class BodyPart {
    String strength;
    List<String> details;
}
