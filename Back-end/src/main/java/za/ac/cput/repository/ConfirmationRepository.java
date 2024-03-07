package za.ac.cput.repository;
import org.springframework.stereotype.Repository;
import za.ac.cput.model.Confirmation;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/13
 * @Time : 18:46
 **/
public interface ConfirmationRepository  <T extends Confirmation> {
      T findTokenByUserId(String userId);
      T save(T t);
      Collection<T> list(String name, int page, int pageSize);
      T read(Long id);
      T update(T t);
      void delete(Long id);
}
