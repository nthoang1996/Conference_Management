/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Tbluser;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
}
