package fr.xebia.xke.micronaut.booking.database;

import fr.xebia.xke.micronaut.booking.domain.ArticleReference;
import fr.xebia.xke.micronaut.booking.domain.Quantity;
import fr.xebia.xke.micronaut.booking.domain.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import static javax.persistence.GenerationType.AUTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "STOCK")
final class StockEntity {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @NotNull
    @Column(name = "ARTICLE", unique = true)
    private String articleReference;

    @NotNull
    @PositiveOrZero
    @Column(name = "QUANTITY")
    private Long quantity;

    Stock toDomain() {
        return Stock.builder()
                .article(new ArticleReference(articleReference))
                .quantity(new Quantity(quantity))
                .build();
    }

}
