package org.example.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.entity.ToDoList;
import org.example.model.User;

import java.time.LocalDate;
import java.util.List;

public class ToDoListDAO {
    private static EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("codemate");

    public static void save(ToDoList todo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(todo);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public static void delete(ToDoList todo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ToDoList managedTodo = em.find(ToDoList.class, todo.getId());

            if (managedTodo != null) {
                em.remove(managedTodo);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public static void update(ToDoList todo) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(todo);   // UPDATE
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }



    public static List<ToDoList> findByUserAndDate(User user, LocalDate date) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM ToDoList t WHERE t.createdAt = :date AND t.user = :user", ToDoList.class)
                    .setParameter("date", date)
                    .setParameter("user", user)
                    .getResultList();
        } finally {
            em.close();
        }
    }


    public static void carryOverPendingTasks(User user,LocalDate today) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Tìm những task chưa xong của những ngày trước
            List<ToDoList> pendingTasks = em.createQuery(
                            "FROM ToDoList WHERE completed = false AND createdAt < :today AND user = :user", ToDoList.class)
                    .setParameter("today", today)
                    .setParameter("user", user)
                    .getResultList();

            // Đổi ngày của chúng thành hôm nay
            for (ToDoList t : pendingTasks) {
                t.setCreatedAt(today);
                em.merge(t); // Cập nhật vào DB
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
