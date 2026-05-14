package org.example.service;

import jakarta.persistence.EntityManager;
import org.example.entity.WaterLog;
import org.example.model.User;
import org.example.utils.DBConnection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class WaterService {
    public void addWater(int amount, User user) {
        EntityManager em = DBConnection.getEntityManager();
        try {
            em.getTransaction().begin();
            LocalDate today = LocalDate.now();

            WaterLog log = em.createQuery("SELECT w FROM WaterLog w WHERE w.date = :d AND w.user = :u", WaterLog.class)
                    .setParameter("d", today)
                    .setParameter("u", user)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (log == null) {
                log = new WaterLog();
                log.setDate(today);
                log.setTotalAmount(amount);
                log.setUser(user);
                em.persist(log);
            } else {
                log.setTotalAmount(log.getTotalAmount() + amount);
                em.merge(log);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public int getTodayTotal(User user) {
        EntityManager em = DBConnection.getEntityManager();
        try {
            Long total = em.createQuery("SELECT SUM(w.totalAmount) FROM WaterLog w WHERE w.date = :today AND w.user = :u", Long.class)
                    .setParameter("today", LocalDate.now())
                    .setParameter("u", user)
                    .getSingleResult();
            return total != null ? total.intValue() : 0;
        } finally {
            em.close();
        }
    }
    public Map<String, Integer> getWeeklyWaterData(User user) {
        EntityManager em = DBConnection.getEntityManager();
        Map<String, Integer> data = new LinkedHashMap<>();
        try {
            for (int i = 6; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                Long total = em.createQuery("SELECT SUM(w.totalAmount) FROM WaterLog w WHERE w.date = :d AND w.user = :u", Long.class)
                        .setParameter("d", date)
                        .setParameter("u", user)
                        .getSingleResult();

                String label = date.format(DateTimeFormatter.ofPattern("dd/MM"));
                data.put(label, total != null ? total.intValue() : 0);
            }
        } finally {
            em.close();
        }
        return data;
    }
}
