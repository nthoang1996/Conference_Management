/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.TblUserDAO.session;
import entity.Tbllocation;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Hoang IT
 */
public class TblLocationDAO {
    static Session session = null;
    public static Tbllocation singleById(int id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from Tbllocation where id = '" + id + "'");
            if (query.list().size() < 1) {
                return null;
            }
            Tbllocation myLocation = (Tbllocation) query.list().get(0);
            return myLocation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            tx.commit();
            session.close();
        }

    }
}
