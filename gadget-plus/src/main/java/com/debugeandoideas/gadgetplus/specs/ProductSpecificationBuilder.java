package com.debugeandoideas.gadgetplus.specs;

import com.debugeandoideas.gadgetplus.dto.ProductSearchCriteria;
import com.debugeandoideas.gadgetplus.entities.CategoryEntity;
import com.debugeandoideas.gadgetplus.entities.ProductCatalogEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ProductSpecificationBuilder {

    //el root es la entidad a la que le vamos a aplicar los criterios
    public Specification<ProductCatalogEntity> build(ProductSearchCriteria criteria) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            //validamos la nulabilidad
            if (Objects.nonNull(criteria.getBrand())) {
                predicates.add(cb.equal(root.get("brand"), criteria.getBrand()));
            }
            if (Objects.nonNull(criteria.getMinPrice())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.getMinPrice()));
            }
            if (Objects.nonNull(criteria.getMaxPrice())) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.getMaxPrice()));
            }
            if (Objects.nonNull(criteria.getMinRating())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), criteria.getMinRating()));
            }
            if (Objects.nonNull(criteria.getHasDiscount())) {
                predicates.add(cb.equal(root.get("isDiscount"), criteria.getHasDiscount()));
            }

            //***************ahora toca category que es un JOIN asi se trabaja con JPA Criteria API*****************
            if (Objects.nonNull(criteria.getCategoryCode())) {
                Join<ProductCatalogEntity, CategoryEntity> categoryJoin =
                        root.join("categories", JoinType.INNER);

                predicates.add(cb.equal(categoryJoin.get("code").as(String.class),//ojo al as(String.class) para que me envie en string
                        criteria.getCategoryCode()));

                //para que no me envie resultados duplicados
                query.distinct(true);
            }

            if (Objects.nonNull(criteria.getLaunchedAfter())) {
                predicates.add(cb.greaterThan(root.get("launchingDate"), criteria.getLaunchedAfter()));
            }

            //ORDER-BY por precio ascendente
            query.orderBy(cb.desc(root.get("rating")), cb.asc(root.get("price")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };// fin del return
    }// build
}//fin ProductSpecificationBuilder
