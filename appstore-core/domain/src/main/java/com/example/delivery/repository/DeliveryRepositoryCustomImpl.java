package com.example.delivery.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryRepositoryCustomImpl implements DeliveryRepositoryCustom{

    private JPAQueryFactory queryFactory;

    @Autowired
    public DeliveryRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }



}
