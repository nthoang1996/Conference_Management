/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entity.Tblconference;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Hoang IT
 */
public class TblConferenceDAO {
    static Session session = null;
    
    public static void insert(Tblconference user){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(user);
        tx.commit();
//        session.flush();
        session.close();
    }
    
    public static List<Tblconference> all(){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        Query query = session.createQuery("from Tblconference");
        if(query.list().size() < 1){
            return null;
        }
        return query.list();
    }
    
    public static void register(int id, int userID){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Tblconference conference = (Tblconference) session.load(Tblconference.class, id);
        tx.commit();

        String query = "UPDATE Tblconference SET participant = '"+ conference.getParticipant() + userID + "," +"' WHERE id = '"+ id + "'";
        SQLQuery sqlQuery = session.createSQLQuery(query);
        sqlQuery.executeUpdate();
        session.close();
    }
    
    public static void unRegister(int id, int userID){
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Tblconference conference = (Tblconference) session.load(Tblconference.class, id);
        String [] arrStr = conference.getParticipant().split(",");
        tx.commit();

        String query = "UPDATE Tblconference SET participant = '"+ conference.getParticipant() + userID + "," +"' WHERE id = '"+ id + "'";
        SQLQuery sqlQuery = session.createSQLQuery(query);
        sqlQuery.executeUpdate();
        session.close();
    }
}
