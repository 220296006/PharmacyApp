package za.ac.cput.model;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Objects;
import static jakarta.persistence.GenerationType.AUTO;

@Data
@SuperBuilder
@AllArgsConstructor
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class Pharmacy implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long pharmacyId;
    private String pharmacyName;
    private String medicationId;
    private String inventoryId;
}
