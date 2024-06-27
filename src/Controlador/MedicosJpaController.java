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
import Modelo.Medicos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author SENA
 */
public class MedicosJpaController implements Serializable {

    public MedicosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicos medicos) throws PreexistingEntityException, Exception {
        if (medicos.getCitasCollection() == null) {
            medicos.setCitasCollection(new ArrayList<Citas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Citas> attachedCitasCollection = new ArrayList<Citas>();
            for (Citas citasCollectionCitasToAttach : medicos.getCitasCollection()) {
                citasCollectionCitasToAttach = em.getReference(citasCollectionCitasToAttach.getClass(), citasCollectionCitasToAttach.getCitNumero());
                attachedCitasCollection.add(citasCollectionCitasToAttach);
            }
            medicos.setCitasCollection(attachedCitasCollection);
            em.persist(medicos);
            for (Citas citasCollectionCitas : medicos.getCitasCollection()) {
                Medicos oldCitMedicoOfCitasCollectionCitas = citasCollectionCitas.getCitMedico();
                citasCollectionCitas.setCitMedico(medicos);
                citasCollectionCitas = em.merge(citasCollectionCitas);
                if (oldCitMedicoOfCitasCollectionCitas != null) {
                    oldCitMedicoOfCitasCollectionCitas.getCitasCollection().remove(citasCollectionCitas);
                    oldCitMedicoOfCitasCollectionCitas = em.merge(oldCitMedicoOfCitasCollectionCitas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMedicos(medicos.getMedIdentificacion()) != null) {
                throw new PreexistingEntityException("Medicos " + medicos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Medicos medicos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicos persistentMedicos = em.find(Medicos.class, medicos.getMedIdentificacion());
            Collection<Citas> citasCollectionOld = persistentMedicos.getCitasCollection();
            Collection<Citas> citasCollectionNew = medicos.getCitasCollection();
            List<String> illegalOrphanMessages = null;
            for (Citas citasCollectionOldCitas : citasCollectionOld) {
                if (!citasCollectionNew.contains(citasCollectionOldCitas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citas " + citasCollectionOldCitas + " since its citMedico field is not nullable.");
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
            medicos.setCitasCollection(citasCollectionNew);
            medicos = em.merge(medicos);
            for (Citas citasCollectionNewCitas : citasCollectionNew) {
                if (!citasCollectionOld.contains(citasCollectionNewCitas)) {
                    Medicos oldCitMedicoOfCitasCollectionNewCitas = citasCollectionNewCitas.getCitMedico();
                    citasCollectionNewCitas.setCitMedico(medicos);
                    citasCollectionNewCitas = em.merge(citasCollectionNewCitas);
                    if (oldCitMedicoOfCitasCollectionNewCitas != null && !oldCitMedicoOfCitasCollectionNewCitas.equals(medicos)) {
                        oldCitMedicoOfCitasCollectionNewCitas.getCitasCollection().remove(citasCollectionNewCitas);
                        oldCitMedicoOfCitasCollectionNewCitas = em.merge(oldCitMedicoOfCitasCollectionNewCitas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = medicos.getMedIdentificacion();
                if (findMedicos(id) == null) {
                    throw new NonexistentEntityException("The medicos with id " + id + " no longer exists.");
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
            Medicos medicos;
            try {
                medicos = em.getReference(Medicos.class, id);
                medicos.getMedIdentificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Citas> citasCollectionOrphanCheck = medicos.getCitasCollection();
            for (Citas citasCollectionOrphanCheckCitas : citasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicos (" + medicos + ") cannot be destroyed since the Citas " + citasCollectionOrphanCheckCitas + " in its citasCollection field has a non-nullable citMedico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(medicos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Medicos> findMedicosEntities() {
        return findMedicosEntities(true, -1, -1);
    }

    public List<Medicos> findMedicosEntities(int maxResults, int firstResult) {
        return findMedicosEntities(false, maxResults, firstResult);
    }

    private List<Medicos> findMedicosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicos.class));
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

    public Medicos findMedicos(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicos.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicos> rt = cq.from(Medicos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
