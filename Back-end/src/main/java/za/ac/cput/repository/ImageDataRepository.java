package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.ac.cput.model.ImageData;

import java.util.Optional;

/**
 * @author : Thabiso Matsaba
 * @Project : Back-end
 * @Date : 2024/04/04
 * @Time : 19:37
 **/
@Repository
public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
    Optional<ImageData> findByNameAndUserId(String fileName, Long userId);
    Optional<ImageData> findByUserId(Long userId);

}
