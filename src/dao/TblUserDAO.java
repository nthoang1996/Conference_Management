/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

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
}
