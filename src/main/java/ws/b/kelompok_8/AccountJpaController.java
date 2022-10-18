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
import ws.b.kelompok_8.exceptions.PreexistingEntityException;

/**
 *
 * @author T.U.F GAMING
 */
public class AccountJpaController implements Serializable {

    public AccountJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Account account) throws PreexistingEntityException, Exception {
        if (account.getTiketCollection() == null) {
            account.setTiketCollection(new ArrayList<Tiket>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Tiket> attachedTiketCollection = new ArrayList<Tiket>();
            for (Tiket tiketCollectionTiketToAttach : account.getTiketCollection()) {
                tiketCollectionTiketToAttach = em.getReference(tiketCollectionTiketToAttach.getClass(), tiketCollectionTiketToAttach.getIdTiket());
                attachedTiketCollection.add(tiketCollectionTiketToAttach);
            }
            account.setTiketCollection(attachedTiketCollection);
            em.persist(account);
            for (Tiket tiketCollectionTiket : account.getTiketCollection()) {
                Account oldUsernameOfTiketCollectionTiket = tiketCollectionTiket.getUsername();
                tiketCollectionTiket.setUsername(account);
                tiketCollectionTiket = em.merge(tiketCollectionTiket);
                if (oldUsernameOfTiketCollectionTiket != null) {
                    oldUsernameOfTiketCollectionTiket.getTiketCollection().remove(tiketCollectionTiket);
                    oldUsernameOfTiketCollectionTiket = em.merge(oldUsernameOfTiketCollectionTiket);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAccount(account.getUsername()) != null) {
                throw new PreexistingEntityException("Account " + account + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Account account) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Account persistentAccount = em.find(Account.class, account.getUsername());
            Collection<Tiket> tiketCollectionOld = persistentAccount.getTiketCollection();
            Collection<Tiket> tiketCollectionNew = account.getTiketCollection();
            List<String> illegalOrphanMessages = null;
            for (Tiket tiketCollectionOldTiket : tiketCollectionOld) {
                if (!tiketCollectionNew.contains(tiketCollectionOldTiket)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tiket " + tiketCollectionOldTiket + " since its username field is not nullable.");
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
            account.setTiketCollection(tiketCollectionNew);
            account = em.merge(account);
            for (Tiket tiketCollectionNewTiket : tiketCollectionNew) {
                if (!tiketCollectionOld.contains(tiketCollectionNewTiket)) {
                    Account oldUsernameOfTiketCollectionNewTiket = tiketCollectionNewTiket.getUsername();
                    tiketCollectionNewTiket.setUsername(account);
                    tiketCollectionNewTiket = em.merge(tiketCollectionNewTiket);
                    if (oldUsernameOfTiketCollectionNewTiket != null && !oldUsernameOfTiketCollectionNewTiket.equals(account)) {
                        oldUsernameOfTiketCollectionNewTiket.getTiketCollection().remove(tiketCollectionNewTiket);
                        oldUsernameOfTiketCollectionNewTiket = em.merge(oldUsernameOfTiketCollectionNewTiket);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = account.getUsername();
                if (findAccount(id) == null) {
                    throw new NonexistentEntityException("The account with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Account account;
            try {
                account = em.getReference(Account.class, id);
                account.getUsername();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The account with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Tiket> tiketCollectionOrphanCheck = account.getTiketCollection();
            for (Tiket tiketCollectionOrphanCheckTiket : tiketCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Account (" + account + ") cannot be destroyed since the Tiket " + tiketCollectionOrphanCheckTiket + " in its tiketCollection field has a non-nullable username field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(account);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Account> findAccountEntities() {
        return findAccountEntities(true, -1, -1);
    }

    public List<Account> findAccountEntities(int maxResults, int firstResult) {
        return findAccountEntities(false, maxResults, firstResult);
    }

    private List<Account> findAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Account.class));
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

    public Account findAccount(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Account.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Account> rt = cq.from(Account.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
