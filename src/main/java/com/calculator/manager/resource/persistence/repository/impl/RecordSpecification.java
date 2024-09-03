package com.calculator.manager.resource.persistence.repository.impl;

import com.calculator.manager.resource.persistence.entity.RecordEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Class to create a filter in JPA and recover data
 */
public class RecordSpecification implements Specification<RecordEntity> {
    private static final Logger logger = LoggerFactory.getLogger(RecordSpecification.class);

    private final String search;
    private final Long userId;
    private final Boolean inactive;

    public RecordSpecification(String search, Long userId, Boolean inactive) {
        this.search = search;
        this.userId = userId;
        this.inactive = inactive;
    }

    /**
     * Predicate method that generate the filter based on the parameters
     * @param root
     * @param query
     * @param criteriaBuilder
     * @return
     */
    @Override
    public Predicate toPredicate(Root<RecordEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        logger.info("Create filter by user by id: {}", userId);
        Predicate predicate = criteriaBuilder.equal(root.get("user").get("id"), userId);

        logger.info("Create filter by field incative = {}", inactive);
        predicate = criteriaBuilder.and(predicate,
                criteriaBuilder.equal(root.get("inactive"), inactive));

        if (search != null && !search.trim().isEmpty()) {
            String likePattern = "%" + search.toLowerCase() + "%";

            logger.info("Create filter by search {} verify operations type", search);
            predicate = criteriaBuilder.and(predicate,
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("operation").get("type")), likePattern));

            logger.info("Create filter by search {} verify operations response", search);
            predicate = criteriaBuilder.or(predicate,
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("operationResponse").as(String.class)), likePattern));

            try {
                Double amount = Double.valueOf(search);
                logger.info("Create filter by search {} verify amount", search);
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(root.get("amount"), amount));

                logger.info("Create filter by search {} verify user balance", search);
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(root.get("userBalance"), amount));
            } catch (NumberFormatException ignored) {
                //Ignore if it can't convert to number
                logger.warn("Unable convert search {} to double", search);
            }

            try {
                LocalDateTime date = LocalDateTime.parse(search);
                logger.info("Create filter by search {} verify date", search);
                predicate = criteriaBuilder.or(predicate, criteriaBuilder.equal(root.get("date"), date));
            } catch (Exception ignored) {
                // Ignore if it can't convert to date
                logger.warn("Unable convert search {} to date", search);
            }
        }

        logger.info("Return predicate filter");
        return predicate;
    }
}
