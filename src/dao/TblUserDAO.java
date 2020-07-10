/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.TblConferenceDAO.session;
import entity.Tbluser;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Hoang IT
 */
public class TblUserDAO {
    static Session session = null;
    
    public static void insert(Tbluser user){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(user);
        tx.commit();
//        session.flush();
        session.close();
    }
    
    public static Tbluser singleByUsername(String username){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        Query query = session.createQuery("from Tbluser where username = '" + username +"'");
        if(query.list().size() < 1){
            return null;
        }
        Tbluser user = (Tbluser)query.list().get(0);
        session.close();
        return user;
    }
    
    public static void update(Tbluser user){
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        System.out.println("dao.TblUserDAO.update()"+ user.getPassword().equals(""));
        System.out.println("dao.TblUserDAO.update()"+ user.getPassword());
        if(user.getPassword().equals("")){
            String hql = "UPDATE Tbluser set name = :name, email = :email "  + 
             "WHERE id = :user_id";
            Query query = session.createQuery(hql);
            query.setParameter("name", user.getName());
            query.setParameter("email", user.getEmail());
            query.setParameter("user_id", user.getId());
            query.executeUpdate();
        }
        else{
            String hql = "UPDATE Tbluser set name = :name, email = :email, password = :password "  + 
             "WHERE id = :user_id";
            Query query = session.createQuery(hql);
            query.setParameter("name", user.getName());
            query.setParameter("email", user.getEmail());
            query.setParameter("password", user.getPassword());
            query.setParameter("user_id", user.getId());
            query.executeUpdate();
        }
        session.getTransaction().commit();
        session.close();
    }
}
