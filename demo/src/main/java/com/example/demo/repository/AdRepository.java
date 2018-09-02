package com.example.demo.repository;

import com.example.demo.domain.Ad;
import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByCategory(String category);
    List<Ad> findByCategoryAndCityOrCategoryAndState(@Param("category")String category1, @Param("city") String city, @Param("category")String category2,@Param("state")String state);
    List<Ad> findByCityOrState(@Param("city") String city, @Param("state")String state);
    List<Ad> findByUser(User user);
    void deleteAdById(Long id);
}
