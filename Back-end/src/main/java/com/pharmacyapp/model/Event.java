package com.pharmacyapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/05/21
 * @Time : 23:58
 **/
@Data
@SuperBuilder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@NoArgsConstructor
public class Event {
    @Id
    private Long id;
    private String type;
    private String description;
}
