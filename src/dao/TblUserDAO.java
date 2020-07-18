/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.TblConferenceDAO.session;
import entity.ConferenceVisible;
import entity.Tblconference;
import entity.Tblregisterconference;
import entity.Tbluser;
import entity.UserVisible;
import java.util.ArrayList;
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

    public static ArrayList<UserVisible> allMember() {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("from Tbluser where role_id = 1 and is_deleted = false");
            if (query.list().size() < 1) {
                return new ArrayList<>();
            } else {
                ArrayList<Tbluser> listUser = (ArrayList< Tbluser>) query.list();
                ArrayList<UserVisible> listUserVisible = new ArrayList<>();
                for (int i = 0; i < listUser.size(); i++) {
                    listUserVisible.add(new UserVisible(listUser.get(i)));
                }
                return (ArrayList< UserVisible>) listUserVisible;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            tx.commit();
            session.close();
        }
    }

    public static ArrayList<UserVisible> allByConferenceId(int idCon) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            ArrayList<Tblregisterconference> listRegister = TblregisterconferenceDAO.allByConferenceIncludeDeny(idCon);
            ArrayList<UserVisible> listUser = new ArrayList<>();
            for (int i = 0; i < listRegister.size(); i++) {
                listUser.add(singleById(listRegister.get(i).getIdUser()));
            }
            return listUser;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            tx.commit();
            session.close();
        }
    }

    public static void insert(Tbluser user) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(user);
        tx.commit();
//        session.flush();
        session.close();
    }

    public static Tbluser singleByUsername(String username) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            Query query = session.createQuery("from Tbluser where username = '" + username + "'");
            if (query.list().size() < 1) {
                return null;
            }
            Tbluser user = (Tbluser) query.list().get(0);

            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            tx.commit();
            session.close();
        }

    }

    public static UserVisible singleById(int id) {
        session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            Query query = session.createQuery("from Tbluser where id = " + id + "");
            if (query.list().size() < 1) {
                return null;
            }
            Tbluser user = (Tbluser) query.list().get(0);
            UserVisible userVisible = new UserVisible(user);
            return userVisible;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            tx.commit();
//            session.close();
        }

    }

    public static void update(Tbluser user) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        if (user.getPassword().equals("")) {
            String hql = "UPDATE Tbluser set name = :name, email = :email "
                    + "WHERE id = :user_id";
            Query query = session.createQuery(hql);
            query.setParameter("name", user.getName());
            query.setParameter("email", user.getEmail());
            query.setParameter("user_id", user.getId());
            query.executeUpdate();
        } else {
            String hql = "UPDATE Tbluser set name = :name, email = :email, password = :password "
                    + "WHERE id = :user_id";
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

    public static boolean disableUser(int idUser) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            String hql = "UPDATE Tbluser set is_disabled = true "
                    + "WHERE id = :user_id";
            Query query = session.createQuery(hql);
            query.setParameter("user_id", idUser);
            query.executeUpdate();
            query = session.createQuery("from Tblregisterconference where id_user = " + idUser);
            if (query.list().size() > 0) {
                ArrayList<Tblregisterconference> listRegister = (ArrayList<Tblregisterconference>) query.list();
                for (int i = 0; i < listRegister.size(); i++) {
                    boolean result = TblregisterconferenceDAO.updateStatus(listRegister.get(i).getId(), 0);
                    if (!result) {
                        throw new Exception("Error has occur");
                    }
                }
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public static boolean enableUser(int idUser) {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            String hql = "UPDATE Tbluser set is_disabled = false "
                    + "WHERE id = :user_id";
            Query query = session.createQuery(hql);
            query.setParameter("user_id", idUser);
            query.executeUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
