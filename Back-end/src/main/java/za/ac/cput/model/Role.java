package za.ac.cput.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/07
 * @Time : 16:58
 **/

@Data
@SuperBuilder
@AllArgsConstructor
@Getter @Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class Role implements Serializable {
    @Id
    private Long id;
    private String name;
    @Getter
    @Setter
    private Set<String> permissions  = new HashSet<>();
}
