package org.example.utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DBConnection {
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManagerFactory buildEntityManagerFactory(){
        try{
            return Persistence.createEntityManagerFactory("codemate");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static EntityManager getEntityManager(){
        if(entityManagerFactory == null || !entityManagerFactory.isOpen()){
            entityManagerFactory = buildEntityManagerFactory();
        }
        return entityManagerFactory.createEntityManager();
    }

    public static void closeEntityManager(){
        if(entityManagerFactory != null || entityManagerFactory.isOpen()){
            entityManagerFactory.close();
        }
    }
}
