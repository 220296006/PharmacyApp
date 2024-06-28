package com.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.io.Serializable;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/04/04
// * @Time : 19:24
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class ImageData implements Serializable {
    @Id
    private Long id;
    private String name;
    private String type;
    @Lob
    @Column(name = "image_data", length = 1000)
    private byte[] imageData;
    @Column(name = "user_id") // Foreign key column
    private Long userId;
}
