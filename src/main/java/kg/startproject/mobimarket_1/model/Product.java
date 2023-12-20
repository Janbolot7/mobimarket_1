package kg.startproject.mobimarket_1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "price", nullable = false)
    BigDecimal price;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    Image image;
    @Column(name = "description")
    String description;
}
