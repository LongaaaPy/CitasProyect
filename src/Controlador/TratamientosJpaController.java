/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Pacientes;
import Modelo.Tratamientos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author SENA
 */
public class TratamientosJpaController implements Serializable {

    public TratamientosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tratamientos tratamientos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pacientes traPaciente = tratamientos.getTraPaciente();
            if (traPaciente != null) {
                traPaciente = em.getReference(traPaciente.getClass(), traPaciente.getPacIdentificacion());
                tratamientos.setTraPaciente(traPaciente);
            }
            em.persist(tratamientos);
            if (traPaciente != null) {
                traPaciente.getTratamientosCollection().add(tratamientos);
                traPaciente = em.merge(traPaciente);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tratamientos tratamientos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tratamientos persistentTratamientos = em.find(Tratamientos.class, tratamientos.getTraNumero());
            Pacientes traPacienteOld = persistentTratamientos.getTraPaciente();
            Pacientes traPacienteNew = tratamientos.getTraPaciente();
            if (traPacienteNew != null) {
                traPacienteNew = em.getReference(traPacienteNew.getClass(), traPacienteNew.getPacIdentificacion());
                tratamientos.setTraPaciente(traPacienteNew);
            }
            tratamientos = em.merge(tratamientos);
            if (traPacienteOld != null && !traPacienteOld.equals(traPacienteNew)) {
                traPacienteOld.getTratamientosCollection().remove(tratamientos);
                traPacienteOld = em.merge(traPacienteOld);
            }
            if (traPacienteNew != null && !traPacienteNew.equals(traPacienteOld)) {
                traPacienteNew.getTratamientosCollection().add(tratamientos);
                traPacienteNew = em.merge(traPacienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tratamientos.getTraNumero();
                if (findTratamientos(id) == null) {
                    throw new NonexistentEntityException("The tratamientos with id " + id + " no longer exists.");
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
            Tratamientos tratamientos;
            try {
                tratamientos = em.getReference(Tratamientos.class, id);
                tratamientos.getTraNumero();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tratamientos with id " + id + " no longer exists.", enfe);
            }
            Pacientes traPaciente = tratamientos.getTraPaciente();
            if (traPaciente != null) {
                traPaciente.getTratamientosCollection().remove(tratamientos);
                traPaciente = em.merge(traPaciente);
            }
            em.remove(tratamientos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tratamientos> findTratamientosEntities() {
        return findTratamientosEntities(true, -1, -1);
    }

    public List<Tratamientos> findTratamientosEntities(int maxResults, int firstResult) {
        return findTratamientosEntities(false, maxResults, firstResult);
    }

    private List<Tratamientos> findTratamientosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tratamientos.class));
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

    public Tratamientos findTratamientos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tratamientos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTratamientosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tratamientos> rt = cq.from(Tratamientos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
