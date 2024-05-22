package za.ac.cput.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/22
 * @Time : 00:02
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class UserEvent {
    @Id
    private Long id;
    private String device;
    private String ipAddress;
    private String createdAt;
    private User user;
    private Event event;
}