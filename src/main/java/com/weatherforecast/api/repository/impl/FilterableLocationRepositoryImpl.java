package com.weatherforecast.api.repository.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.weatherforecast.api.entity.Location;
import com.weatherforecast.api.repository.FilterableLocationRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
@Repository
public class FilterableLocationRepositoryImpl implements FilterableLocationRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<Location> listWithFilter(Pageable pageable, Map<String, Object> filterFields) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> query = builder.createQuery(Location.class);

        Root<Location> root = query.from(Location.class);
        Predicate[] predicates = createPredicates(filterFields, builder, root);
        if (predicates.length > 0) query.where(predicates);
        List<Order> orderList = new ArrayList<>();

        pageable.getSort().forEach(order -> {
            System.out.println("Sorting by: " + order.getProperty());
            orderList.add(builder.asc(root.get(order.getProperty())));
        });
        query.orderBy(orderList);

        TypedQuery<Location> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Location> listResults = typedQuery.getResultList();

        long totalRows = getTotalRow(filterFields);

        return new PageImpl<>(listResults, pageable, totalRows);
    }

    private Predicate[] createPredicates(Map<String, Object> filterFields, CriteriaBuilder builder, Root<Location> root) {

        Predicate[] predicates = new Predicate[filterFields.size() + 1];

        if (filterFields.isEmpty()) {
            Iterator<String> iterator = filterFields.keySet().iterator();
            int i = 0;

            while (iterator.hasNext()) {
                String fieldName = iterator.next();
                Object fieldValue = filterFields.get(fieldName);

                System.out.println(fieldName + " => " + fieldValue);
                predicates[i++] = builder.equal(root.get(fieldName), fieldValue);
            }
        }

        predicates[predicates.length - 1] = builder.equal(root.get("trashed"), false);
        return predicates;
    }

    private long getTotalRow(Map<String, Object> filterFields) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

        Root<Location> root = countQuery.from(Location.class);
        countQuery.select(builder.count(root));

        Predicate[] predicates = createPredicates(filterFields, builder, root);
        if (predicates.length > 0) countQuery.where(predicates);

        Long rowCount = entityManager.createQuery(countQuery).getSingleResult();
        return rowCount;
    }

}
