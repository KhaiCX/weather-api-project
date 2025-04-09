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
        if (filterFields.isEmpty()) {
            Predicate[] predicate = new Predicate[filterFields.size()];
            Iterator<String> iterator = filterFields.keySet().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                String fieldName = iterator.next();
                Object fieldValue = filterFields.get(fieldName);

                System.out.println(fieldName + " => " + fieldValue);
                predicate[i++] = builder.equal(root.get(fieldName), fieldValue);
            }
            query.where(predicate);
        }
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

        int totalRows = 0;

        return new PageImpl<>(listResults, pageable, totalRows);
    }

}
