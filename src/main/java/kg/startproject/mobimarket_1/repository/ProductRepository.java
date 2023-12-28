package kg.startproject.mobimarket_1.repository;

import kg.startproject.mobimarket_1.model.Product;
import kg.startproject.mobimarket_1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByUser(User user);
    @Modifying
    @Query("UPDATE Product p SET p.likesCount = :likesCount WHERE p.id = :productId")
    void updateLikesCount(@Param("productId") Integer productId, @Param("likesCount") int likesCount);

}