package com.iotcitybackend.repository.impl;

import com.iotcitybackend.model.SensorData;
import com.iotcitybackend.repository.SensorDataRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SensorDataRepositoryImpl implements SensorDataRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SensorData> findWithFilters(String sensorType, Long deviceId, LocalDateTime start, LocalDateTime end) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SensorData> query = cb.createQuery(SensorData.class);
        Root<SensorData> sensorData = query.from(SensorData.class);

        List<Predicate> predicates = new ArrayList<>();

        if (sensorType != null && !sensorType.isEmpty()) {
            predicates.add(cb.equal(sensorData.get("sensorType"), sensorType));
        }
        if (deviceId != null) {
            predicates.add(cb.equal(sensorData.get("device").get("id"), deviceId));
        }
        if (start != null) {
            predicates.add(cb.greaterThanOrEqualTo(sensorData.get("timestamp"), start));
        }
        if (end != null) {
            predicates.add(cb.lessThanOrEqualTo(sensorData.get("timestamp"), end));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.orderBy(cb.desc(sensorData.get("timestamp")));

        return entityManager.createQuery(query).getResultList();
    }
} 