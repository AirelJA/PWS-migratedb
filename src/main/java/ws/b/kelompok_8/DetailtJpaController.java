/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.b.kelompok_8;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import ws.b.kelompok_8.exceptions.IllegalOrphanException;
import ws.b.kelompok_8.exceptions.NonexistentEntityException;

/**
 *
 * @author T.U.F GAMING
 */
public class DetailtJpaController implements Serializable {

    public DetailtJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detailt detailt) {
        if (detailt.getTiketCollection() == null) {
            detailt.setTiketCollection(new ArrayList<Tiket>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Tiket> attachedTiketCollection = new ArrayList<Tiket>();
            for (Tiket tiketCollectionTiketToAttach : detailt.getTiketCollection()) {
                tiketCollectionTiketToAttach = em.getReference(tiketCollectionTiketToAttach.getClass(), tiketCollectionTiketToAttach.getIdTiket());
                attachedTiketCollection.add(tiketCollectionTiketToAttach);
            }
            detailt.setTiketCollection(attachedTiketCollection);
            em.persist(detailt);
            for (Tiket tiketCollectionTiket : detailt.getTiketCollection()) {
                Detailt oldIddetailTOfTiketCollectionTiket = tiketCollectionTiket.getIddetailT();
                tiketCollectionTiket.setIddetailT(detailt);
                tiketCollectionTiket = em.merge(tiketCollectionTiket);
                if (oldIddetailTOfTiketCollectionTiket != null) {
                    oldIddetailTOfTiketCollectionTiket.getTiketCollection().remove(tiketCollectionTiket);
                    oldIddetailTOfTiketCollectionTiket = em.merge(oldIddetailTOfTiketCollectionTiket);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detailt detailt) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detailt persistentDetailt = em.find(Detailt.class, detailt.getIddetailT());
            Collection<Tiket> tiketCollectionOld = persistentDetailt.getTiketCollection();
            Collection<Tiket> tiketCollectionNew = detailt.getTiketCollection();
            List<String> illegalOrphanMessages = null;
            for (Tiket tiketCollectionOldTiket : tiketCollectionOld) {
                if (!tiketCollectionNew.contains(tiketCollectionOldTiket)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tiket " + tiketCollectionOldTiket + " since its iddetailT field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Tiket> attachedTiketCollectionNew = new ArrayList<Tiket>();
            for (Tiket tiketCollectionNewTiketToAttach : tiketCollectionNew) {
                tiketCollectionNewTiketToAttach = em.getReference(tiketCollectionNewTiketToAttach.getClass(), tiketCollectionNewTiketToAttach.getIdTiket());
                attachedTiketCollectionNew.add(tiketCollectionNewTiketToAttach);
            }
            tiketCollectionNew = attachedTiketCollectionNew;
            detailt.setTiketCollection(tiketCollectionNew);
            detailt = em.merge(detailt);
            for (Tiket tiketCollectionNewTiket : tiketCollectionNew) {
                if (!tiketCollectionOld.contains(tiketCollectionNewTiket)) {
                    Detailt oldIddetailTOfTiketCollectionNewTiket = tiketCollectionNewTiket.getIddetailT();
                    tiketCollectionNewTiket.setIddetailT(detailt);
                    tiketCollectionNewTiket = em.merge(tiketCollectionNewTiket);
                    if (oldIddetailTOfTiketCollectionNewTiket != null && !oldIddetailTOfTiketCollectionNewTiket.equals(detailt)) {
                        oldIddetailTOfTiketCollectionNewTiket.getTiketCollection().remove(tiketCollectionNewTiket);
                        oldIddetailTOfTiketCollectionNewTiket = em.merge(oldIddetailTOfTiketCollectionNewTiket);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detailt.getIddetailT();
                if (findDetailt(id) == null) {
                    throw new NonexistentEntityException("The detailt with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detailt detailt;
            try {
                detailt = em.getReference(Detailt.class, id);
                detailt.getIddetailT();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detailt with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Tiket> tiketCollectionOrphanCheck = detailt.getTiketCollection();
            for (Tiket tiketCollectionOrphanCheckTiket : tiketCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Detailt (" + detailt + ") cannot be destroyed since the Tiket " + tiketCollectionOrphanCheckTiket + " in its tiketCollection field has a non-nullable iddetailT field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(detailt);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detailt> findDetailtEntities() {
        return findDetailtEntities(true, -1, -1);
    }

    public List<Detailt> findDetailtEntities(int maxResults, int firstResult) {
        return findDetailtEntities(false, maxResults, firstResult);
    }

    private List<Detailt> findDetailtEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detailt.class));
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

    public Detailt findDetailt(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detailt.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetailtCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detailt> rt = cq.from(Detailt.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
