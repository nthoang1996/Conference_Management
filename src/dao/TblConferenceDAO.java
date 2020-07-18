/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conferencemanagement.utils.GlobalData;
import conferencemanagement.utils.Helper;
import static dao.TblUserDAO.session;
import static dao.TblregisterconferenceDAO.session;
import entity.ConferenceVisible;
import entity.MyConferenceItem;
import entity.Tblconference;
import entity.Tblregisterconference;
import entity.Tbluser;
import java.io.File;
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

    public static boolean insert(Tblconference conference, String imagePath) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(conference);
            String path = System.getProperty("user.dir") + "/src/asset/picture/" + conference.getId();
            File file = new File(path);
            //Creating the directory
            boolean bool = file.mkdir();
            if (!bool) {
                return false;
            }
            String filePathString = path + "/main.png";
            file = new File(filePathString);
            file.createNewFile();
            Helper.createImage(filePathString, imagePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            return false;
        } finally {
            tx.commit();
            session.close();
        }
    }

    public static boolean update(Tblconference conference, String imagePath) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            String hql = "UPDATE Tblconference set name = :name, overview = :overview, description = :description, location_id = :location_id, start_time = :start_time, end_time = :end_time "
                    + "WHERE id = :conference_id";
            Query query = session.createQuery(hql);
            query.setParameter("name", conference.getName());
            query.setParameter("overview", conference.getOverview());
            query.setParameter("description", conference.getDescription());
            query.setParameter("location_id", conference.getLocationId());
            query.setParameter("start_time", conference.getStartTime());
            query.setParameter("end_time", conference.getEndTime());
            query.setParameter("conference_id", conference.getId());
            query.executeUpdate();
            if (!imagePath.equals("")) {
                String path = System.getProperty("user.dir") + "/src/asset/picture/" + conference.getId();
                File file = new File(path);
                //Creating the directory
                String filePathString = path + "/main.png";
                file = new File(filePathString);
                file.createNewFile();
                Helper.createImage(filePathString, imagePath);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
            return false;
        } finally {
            tx.commit();
            session.close();
        }
    }

    public static ArrayList<ConferenceVisible> all() {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Tblconference");
        ArrayList<Tblconference> listConference = (ArrayList< Tblconference>) query.list();
        tx.commit();
        if (listConference.size() < 1) {
            return new ArrayList<>();
        }
        ArrayList<ConferenceVisible> listConferenceVisible = new ArrayList<>();
        for (int i = 0; i < listConference.size(); i++) {
            listConferenceVisible.add(new ConferenceVisible(listConference.get(i)));
        }
        return (ArrayList< ConferenceVisible>) listConferenceVisible;
    }

    public static ArrayList<ConferenceVisible> allIncludeDeny() {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Tblconference");
        ArrayList<Tblconference> listConference = (ArrayList< Tblconference>) query.list();
        tx.commit();
        if (listConference.size() < 1) {
            return new ArrayList<>();
        }
        ArrayList<ConferenceVisible> listConferenceVisible = new ArrayList<>();
        for (int i = 0; i < listConference.size(); i++) {
            listConferenceVisible.add(new ConferenceVisible(listConference.get(i), true));
        }
        return (ArrayList< ConferenceVisible>) listConferenceVisible;
    }

    public static ArrayList<MyConferenceItem> allByUserID(int idUser) {
        ArrayList<Tblregisterconference> register = TblregisterconferenceDAO.allByUser(idUser);
        if (register == null) {
            return new ArrayList<>();
        }
        try {
            ArrayList<MyConferenceItem> listMyConferenceItem = new ArrayList<>();
            for (int i = 0; i < register.size(); i++) {
                listMyConferenceItem.add(new MyConferenceItem(singleById(register.get(i).getIdConference()), register.get(i).getStatus()));
            }
            return (ArrayList< MyConferenceItem>) listMyConferenceItem;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static Tblconference singleById(int id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from Tblconference where id = '" + id + "'");
            if (query.list().size() < 1) {
                return null;
            }
            Tblconference conference = (Tblconference) query.list().get(0);
            return conference;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            tx.commit();
            session.close();
        }

    }

    public static ConferenceVisible singleByIdConAndIDUser(int idCon, int idUser) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from Tblconference where id = '" + idCon + "'");
            if (query.list().size() < 1) {
                return null;
            }
            Tblconference conference = (Tblconference) query.list().get(0);
            return new ConferenceVisible(conference, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            tx.commit();
            session.close();
        }

    }
    
    public static ArrayList<ConferenceVisible> allByLocationId(int idCon) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Tblconference where location_id =" + idCon);
        ArrayList<Tblconference> listConference = (ArrayList< Tblconference>) query.list();
        tx.commit();
        if (listConference.size() < 1) {
            return new ArrayList<>();
        }
        ArrayList<ConferenceVisible> listConferenceVisible = new ArrayList<>();
        for (int i = 0; i < listConference.size(); i++) {
            listConferenceVisible.add(new ConferenceVisible(listConference.get(i), true));
        }
        return (ArrayList< ConferenceVisible>) listConferenceVisible;
    }
    
    public static ArrayList<ConferenceVisible> allByLocationIdWithoutThis(int idLoc, int idCon) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from Tblconference where location_id =" + idLoc + "and id <> " + idCon);
        ArrayList<Tblconference> listConference = (ArrayList< Tblconference>) query.list();
        tx.commit();
        if (listConference.size() < 1) {
            return new ArrayList<>();
        }
        ArrayList<ConferenceVisible> listConferenceVisible = new ArrayList<>();
        for (int i = 0; i < listConference.size(); i++) {
            listConferenceVisible.add(new ConferenceVisible(listConference.get(i), true));
        }
        return (ArrayList< ConferenceVisible>) listConferenceVisible;
    }

}
