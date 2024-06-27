/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.IllegalOrphanException;
import Controlador.exceptions.NonexistentEntityException;
import Controlador.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Citas;
import Modelo.Consultorios;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author SENA
 */
public class ConsultoriosJpaController implements Serializable {

    public ConsultoriosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consultorios consultorios) throws PreexistingEntityException, Exception {
        if (consultorios.getCitasCollection() == null) {
            consultorios.setCitasCollection(new ArrayList<Citas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Citas> attachedCitasCollection = new ArrayList<Citas>();
            for (Citas citasCollectionCitasToAttach : consultorios.getCitasCollection()) {
                citasCollectionCitasToAttach = em.getReference(citasCollectionCitasToAttach.getClass(), citasCollectionCitasToAttach.getCitNumero());
                attachedCitasCollection.add(citasCollectionCitasToAttach);
            }
            consultorios.setCitasCollection(attachedCitasCollection);
            em.persist(consultorios);
            for (Citas citasCollectionCitas : consultorios.getCitasCollection()) {
                Consultorios oldCitConsultorioOfCitasCollectionCitas = citasCollectionCitas.getCitConsultorio();
                citasCollectionCitas.setCitConsultorio(consultorios);
                citasCollectionCitas = em.merge(citasCollectionCitas);
                if (oldCitConsultorioOfCitasCollectionCitas != null) {
                    oldCitConsultorioOfCitasCollectionCitas.getCitasCollection().remove(citasCollectionCitas);
                    oldCitConsultorioOfCitasCollectionCitas = em.merge(oldCitConsultorioOfCitasCollectionCitas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConsultorios(consultorios.getConNumero()) != null) {
                throw new PreexistingEntityException("Consultorios " + consultorios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Consultorios consultorios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consultorios persistentConsultorios = em.find(Consultorios.class, consultorios.getConNumero());
            Collection<Citas> citasCollectionOld = persistentConsultorios.getCitasCollection();
            Collection<Citas> citasCollectionNew = consultorios.getCitasCollection();
            List<String> illegalOrphanMessages = null;
            for (Citas citasCollectionOldCitas : citasCollectionOld) {
                if (!citasCollectionNew.contains(citasCollectionOldCitas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citas " + citasCollectionOldCitas + " since its citConsultorio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Citas> attachedCitasCollectionNew = new ArrayList<Citas>();
            for (Citas citasCollectionNewCitasToAttach : citasCollectionNew) {
                citasCollectionNewCitasToAttach = em.getReference(citasCollectionNewCitasToAttach.getClass(), citasCollectionNewCitasToAttach.getCitNumero());
                attachedCitasCollectionNew.add(citasCollectionNewCitasToAttach);
            }
            citasCollectionNew = attachedCitasCollectionNew;
            consultorios.setCitasCollection(citasCollectionNew);
            consultorios = em.merge(consultorios);
            for (Citas citasCollectionNewCitas : citasCollectionNew) {
                if (!citasCollectionOld.contains(citasCollectionNewCitas)) {
                    Consultorios oldCitConsultorioOfCitasCollectionNewCitas = citasCollectionNewCitas.getCitConsultorio();
                    citasCollectionNewCitas.setCitConsultorio(consultorios);
                    citasCollectionNewCitas = em.merge(citasCollectionNewCitas);
                    if (oldCitConsultorioOfCitasCollectionNewCitas != null && !oldCitConsultorioOfCitasCollectionNewCitas.equals(consultorios)) {
                        oldCitConsultorioOfCitasCollectionNewCitas.getCitasCollection().remove(citasCollectionNewCitas);
                        oldCitConsultorioOfCitasCollectionNewCitas = em.merge(oldCitConsultorioOfCitasCollectionNewCitas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consultorios.getConNumero();
                if (findConsultorios(id) == null) {
                    throw new NonexistentEntityException("The consultorios with id " + id + " no longer exists.");
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
            Consultorios consultorios;
            try {
                consultorios = em.getReference(Consultorios.class, id);
                consultorios.getConNumero();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consultorios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Citas> citasCollectionOrphanCheck = consultorios.getCitasCollection();
            for (Citas citasCollectionOrphanCheckCitas : citasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Consultorios (" + consultorios + ") cannot be destroyed since the Citas " + citasCollectionOrphanCheckCitas + " in its citasCollection field has a non-nullable citConsultorio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(consultorios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Consultorios> findConsultoriosEntities() {
        return findConsultoriosEntities(true, -1, -1);
    }

    public List<Consultorios> findConsultoriosEntities(int maxResults, int firstResult) {
        return findConsultoriosEntities(false, maxResults, firstResult);
    }

    private List<Consultorios> findConsultoriosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consultorios.class));
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

    public Consultorios findConsultorios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consultorios.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultoriosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consultorios> rt = cq.from(Consultorios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
