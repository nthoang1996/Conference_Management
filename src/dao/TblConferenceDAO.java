/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conferencemanagement.utils.GlobalData;
import conferencemanagement.utils.Helper;
import entity.Tblconference;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static void insert(Tblconference user) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(user);
        tx.commit();
//        session.flush();
        session.close();
    }

    public static List<Tblconference> all() {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Tblconference");
        if (query.list().size() < 1) {
            return null;
        }
        return query.list();
    }
    
    public static ArrayList<Tblconference> allByUser(){
        List<Tblconference> listConference = all();
        ArrayList<Tblconference> listMyConference = new ArrayList<>();
        for (int i=0; i<listConference.size(); i++){
            String[] arrPar = listConference.get(i).getParticipant().split(",");
            if(Helper.checkInclude(GlobalData.currentUser.getId() + "", arrPar)){
                listMyConference.add(listConference.get(i));
            }
        }
        return listMyConference;
    }

    public static void register(int id, String dataQuery) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        String hql = "UPDATE Tblconference set participant = :participant "  + 
             "WHERE id = :conference_id";
        Query query = session.createQuery(hql);
        query.setParameter("participant", dataQuery);
        query.setParameter("conference_id", id);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public static void unRegister(int id, String dataQuery) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        String hql = "UPDATE Tblconference set participant = :participant "  + 
             "WHERE id = :conference_id";
        Query query = session.createQuery(hql);
        query.setParameter("participant", dataQuery);
        query.setParameter("conference_id", id);
        query.executeUpdate();
        tx1.commit();
        session.close();
    }
}
