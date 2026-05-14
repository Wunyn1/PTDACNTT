package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import org.example.entity.PomodoroState;
import org.example.entity.TimeManagements;
import org.example.model.User;
import org.example.model.UserSession;
import org.example.utils.DBConnection;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map;
import java.util.LinkedHashMap;


public class StatService {
    @FXML private BarChart<String, Number> waterChart;
    private final org.example.service.WaterService waterService = new org.example.service.WaterService();

    public void saveSession(long seconds) {
        EntityManager em = DBConnection.getEntityManager();
        EntityTransaction entityTransaction = em.getTransaction();
        try {
            entityTransaction.begin();
            TimeManagements time = new TimeManagements();
            time.setUser(UserSession.getCurrentUser());
            time.setStart_time(LocalDateTime.now());
            time.setDuration_seconds(seconds);
            time.setState(PomodoroState.WORK);

            em.persist(time);
            entityTransaction.commit();
        } catch (Exception e) {
            if (entityTransaction.isActive()) entityTransaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public Map<String, Long> getStatByUserId(long userId){
        EntityManager em = DBConnection.getEntityManager();
        Map<String,Long> map = new HashMap<>();

        try{
            String jpql = "select function('date',t.start_time),sum(t.duration_seconds) "
                    + "from TimeManagements t " +
                    "where t.user.id = :uid " +
                    "group by function('date', t.start_time)";
            List<Object[]> results = em.createQuery(jpql)
                    .setParameter("uid", userId)
                    .getResultList();

            for(Object[] re : results){
                map.put(re[0].toString(), ((Number)re[1]).longValue());

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            em.close();
        }
        return map;
    }

    private void loadWaterData() {
        User currentUser = UserSession.getCurrentUser();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Lượng nước (ml)");

        Map<String, Integer> data = waterService.getWeeklyWaterData(currentUser);

        data.forEach((date, amount) -> {
            series.getData().add(new XYChart.Data<>(date, amount));
        });

        waterChart.getData().clear();
        waterChart.getData().add(series);
    }

    public Map<String, Integer> getWeeklyPomoData() {
        EntityManager em = DBConnection.getEntityManager();
        Map<String, Integer> data = new LinkedHashMap<>();
        try {
            Long userId = UserSession.getCurrentUser().getId();
            DateTimeFormatter labelFormatter = DateTimeFormatter.ofPattern("dd/MM");

            for (int i = 6; i >= 0; i--) {
                LocalDate date = LocalDate.now().minusDays(i);
                String label = date.format(labelFormatter);

                Long totalSeconds = em.createQuery(
                                "SELECT SUM(t.duration_seconds) FROM TimeManagements t " +
                                        "WHERE t.user.id = :uid AND FUNCTION('date', t.start_time) = :d", Long.class)
                        .setParameter("uid", userId)
                        .setParameter("d", java.sql.Date.valueOf(date))
                        .getSingleResult();

                // Đổi giây sang phút
                int minutes = (totalSeconds != null) ? (int) (totalSeconds / 60) : 0;
                data.put(label, minutes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return data;
    }
}
