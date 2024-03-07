package za.ac.cput.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 18:42
 **/
@NoArgsConstructor
@Setter @Getter
@AllArgsConstructor
@Data
public class Confirmation {
    @Id
    private Long id;
    private String token;
    @CreatedDate
    private LocalDateTime createdDate;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
}
