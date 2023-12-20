package kg.startproject.mobimarket_1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ImageDto {

    @JsonProperty("imageName")
    String imageName;

    @JsonProperty("imageData")
    String imageData;

    @JsonProperty("imageExtension")
    String imageExtension;

}
