package za.ac.cput.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
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
@EnableJpaRepositories
public interface ImageDataRepository extends JpaRepository<ImageData, Long> {
    @Query("SELECT i FROM ImageData i WHERE i.userId = :userId")
    Optional<ImageData> findByUserId(@Param("userId") Long userId);

    @Query("SELECT i FROM ImageData i WHERE i.name = :fileName AND i.userId = :userId")
    Optional<ImageData> findByNameAndUserId(@Param("fileName") String fileName, @Param("userId") Long userId);
}
