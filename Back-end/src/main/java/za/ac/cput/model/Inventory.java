package za.ac.cput.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 16:33
 **/

@Data
@SuperBuilder
@AllArgsConstructor
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class Inventory {
    @Id
    private Long id;
    private String name;
    private String description;
    private BigInteger quantity;
    private String price;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Medication medication;
}
