package kg.startproject.mobimarket_1.model;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int product_id;
    private String productName;
    private int price;
    private String shortDescription;
    private String fullDescription;
    private int numberOfLikes;

    @Lob
    private String image;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @ManyToMany(mappedBy = "favoriteProducts")
    private Set<User> favoriteByUsers = new HashSet<>();


    public void incrementLikeCount() {
        numberOfLikes++;
    }

    public void decrementLikeCount() {
        if (numberOfLikes > 0) {
            numberOfLikes--;
        }
    }

}
