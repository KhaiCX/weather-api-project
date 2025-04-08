package com.weatherforecast.api.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.weatherforecast.api.entity.Location;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LocationCriteriaQueryTests {
    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCriteriaQuery() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> query = builder.createQuery(Location.class);

        Root<Location> root = query.from(Location.class);

        //Add WHERE clause
        Predicate predicate = builder.equal(root.get("countryCode"), "US");
        query.where(predicate);

        //Add Order By Clause
        query.orderBy(builder.asc(root.get("cityName")));

        //Add Pagination
        TypedQuery<Location> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(0);
        typedQuery.setMaxResults(1);
        
        List<Location> locations = typedQuery.getResultList();

        locations.forEach(System.out::println);
        assertFalse(locations.isEmpty());
    }

    @Test
    public void testJPQL() {
        String jpql = "FROM Location WHERE countryCode= 'US' ORDER BY cityName ASC";
        TypedQuery<Location> typedQuery = entityManager.createQuery(jpql, Location.class);
        List<Location> locations = typedQuery.getResultList();

        locations.forEach(System.out::println);
        assertFalse(locations.isEmpty());
    }

}
