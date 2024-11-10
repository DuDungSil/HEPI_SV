package org.hepi.hepi_sv.web.dto.exercise;

import java.util.List;

import lombok.Data;

@Data
public class BodyPart {
    String strength;
    List<String> details;
}
