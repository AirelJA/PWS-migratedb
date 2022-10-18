/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.kelompok_8;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ws.b.kelompok_8.exceptions.NonexistentEntityException;

/**
 *
 * @author T.U.F GAMING
 */
public class TiketJpaController implements Serializable {

    public TiketJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tiket tiket) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detailt iddetailT = tiket.getIddetailT();
            if (iddetailT != null) {
                iddetailT = em.getReference(iddetailT.getClass(), iddetailT.getIddetailT());
                tiket.setIddetailT(iddetailT);
            }
            Account username = tiket.getUsername();
            if (username != null) {
                username = em.getReference(username.getClass(), username.getUsername());
                tiket.setUsername(username);
            }
            em.persist(tiket);
            if (iddetailT != null) {
                iddetailT.getTiketCollection().add(tiket);
                iddetailT = em.merge(iddetailT);
            }
            if (username != null) {
                username.getTiketCollection().add(tiket);
                username = em.merge(username);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tiket tiket) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiket persistentTiket = em.find(Tiket.class, tiket.getIdTiket());
            Detailt iddetailTOld = persistentTiket.getIddetailT();
            Detailt iddetailTNew = tiket.getIddetailT();
            Account usernameOld = persistentTiket.getUsername();
            Account usernameNew = tiket.getUsername();
            if (iddetailTNew != null) {
                iddetailTNew = em.getReference(iddetailTNew.getClass(), iddetailTNew.getIddetailT());
                tiket.setIddetailT(iddetailTNew);
            }
            if (usernameNew != null) {
                usernameNew = em.getReference(usernameNew.getClass(), usernameNew.getUsername());
                tiket.setUsername(usernameNew);
            }
            tiket = em.merge(tiket);
            if (iddetailTOld != null && !iddetailTOld.equals(iddetailTNew)) {
                iddetailTOld.getTiketCollection().remove(tiket);
                iddetailTOld = em.merge(iddetailTOld);
            }
            if (iddetailTNew != null && !iddetailTNew.equals(iddetailTOld)) {
                iddetailTNew.getTiketCollection().add(tiket);
                iddetailTNew = em.merge(iddetailTNew);
            }
            if (usernameOld != null && !usernameOld.equals(usernameNew)) {
                usernameOld.getTiketCollection().remove(tiket);
                usernameOld = em.merge(usernameOld);
            }
            if (usernameNew != null && !usernameNew.equals(usernameOld)) {
                usernameNew.getTiketCollection().add(tiket);
                usernameNew = em.merge(usernameNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiket.getIdTiket();
                if (findTiket(id) == null) {
                    throw new NonexistentEntityException("The tiket with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tiket tiket;
            try {
                tiket = em.getReference(Tiket.class, id);
                tiket.getIdTiket();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiket with id " + id + " no longer exists.", enfe);
            }
            Detailt iddetailT = tiket.getIddetailT();
            if (iddetailT != null) {
                iddetailT.getTiketCollection().remove(tiket);
                iddetailT = em.merge(iddetailT);
            }
            Account username = tiket.getUsername();
            if (username != null) {
                username.getTiketCollection().remove(tiket);
                username = em.merge(username);
            }
            em.remove(tiket);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tiket> findTiketEntities() {
        return findTiketEntities(true, -1, -1);
    }

    public List<Tiket> findTiketEntities(int maxResults, int firstResult) {
        return findTiketEntities(false, maxResults, firstResult);
    }

    private List<Tiket> findTiketEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiket.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tiket findTiket(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tiket.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiketCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiket> rt = cq.from(Tiket.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
