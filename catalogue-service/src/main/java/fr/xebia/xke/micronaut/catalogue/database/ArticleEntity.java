package fr.xebia.xke.micronaut.catalogue.database;

import fr.xebia.xke.micronaut.catalogue.domain.Article;
import fr.xebia.xke.micronaut.catalogue.domain.ArticleReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import static fr.xebia.xke.micronaut.catalogue.domain.Price.cents;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ARTICLE")
final class ArticleEntity {

    @Id
    @Column(name = "REFERENCE", unique = true)
    private String articleReference;

    @NotNull
    @PositiveOrZero
    @Column(name = "PRICE")
    private Long price;

    Article toDomain() {
        return Article.builder()
                .reference(new ArticleReference(articleReference))
                .referencePrice(cents(price))
                .build();
    }

}
