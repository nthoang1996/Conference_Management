/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.TblUserDAO.session;
import entity.Tblregisterconference;
import entity.Tbluser;
import java.util.ArrayList;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Hoang IT
 */
public class TblregisterconferenceDAO {

    static Session session = null;

    public static ArrayList<Tblregisterconference> allByConference(int idCon) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from Tblregisterconference where id_conference = '" + idCon + "' and status <> 0");
            if (query.list().size() < 1) {
                return new ArrayList<>();
            }
            ArrayList<Tblregisterconference> list = (ArrayList<Tblregisterconference>) query.list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        finally{
            tx.commit();
            session.close();
        }
    }

    public static ArrayList<Tblregisterconference> allByUser(int idUser) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Tblregisterconference where id_user = '" + idUser + "' and status <> 0");
        if (query.list().size() < 1) {
            return null;
        }
        ArrayList<Tblregisterconference> list = (ArrayList<Tblregisterconference>) query.list();
        session.close();
        return list;
    }

    public static Tblregisterconference singleByConferenceAndUser(int idCon, int idUser) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Tblregisterconference where id_conference = '" + idCon + "' and id_user = '" + idUser + "'");
        if (query.list().size() < 1) {
            return null;
        }
        ArrayList<Tblregisterconference> list = (ArrayList<Tblregisterconference>) query.list();
        session.close();
        return list.get(0);
    }

    public static void insert(int idCon, int idUser) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Tblregisterconference register = new Tblregisterconference(idCon, idUser, 1);
        session.save(register);
        tx.commit();
//        session.flush();
        session.close();
    }

    public static boolean updateStatus(int idCon, int idUser, int status) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        try {
            String hql = "UPDATE Tblregisterconference set status = :status "
                    + "WHERE id_conference = :conference_id and id_user = :user_id";
            Query query = session.createQuery(hql);
            query.setParameter("status", status);
            query.setParameter("conference_id", idCon);
            query.setParameter("user_id", idUser);
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            tx1.commit();
            session.close();
        }

    }
}
