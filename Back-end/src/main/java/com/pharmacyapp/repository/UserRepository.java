package com.pharmacyapp.repository;

import com.pharmacyapp.model.User;

import java.util.Collection;

/**
 * @author : Thabiso Matsaba
 * @Project : PharmacyApp
 * @Date : 2023/07/08
 * @Time : 15:00
 **/
public interface UserRepository <T extends User> {
      T save(T t);
      Collection<T> list(String name, int page, int pageSize);
      T read(Long id);
      T update(User t);
      void delete(Long id);
      T findUserByEmailIgnoreCase(String email);
      Integer countUsers();
      void updateUserImageUrl(Long userId, String imageUrl);
}
