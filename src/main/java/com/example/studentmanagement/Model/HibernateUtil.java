package com.example.studentmanagement.Model;

import com.example.studentmanagement.Model.Student.*;
import com.example.studentmanagement.Model.Student.Class;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;
    private static Session session;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Student.class);
                configuration.addAnnotatedClass(Class.class);
                configuration.addAnnotatedClass(Marks.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(StudentConditionMap.class);
                configuration.addAnnotatedClass(User.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                registry = builder.build();
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }

    public static Session getSession(){
        if(session==null){
            try{
                session=getSessionFactory().openSession();
            } catch (Exception e){
                System.out.println(e);
                e.printStackTrace();
            }
        }
        return session;
    }
    public static void closeSession(){
        if(session!=null){
            session.close();
            session=null;
        }
    }

    public static void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
