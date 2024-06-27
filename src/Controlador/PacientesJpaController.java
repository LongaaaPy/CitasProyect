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
import Modelo.Pacientes;
import java.util.ArrayList;
import java.util.Collection;
import Modelo.Tratamientos;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


/**
 *
 * @author SENA
 */
public class PacientesJpaController implements Serializable {

    public PacientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pacientes pacientes) throws PreexistingEntityException, Exception {
        if (pacientes.getCitasCollection() == null) {
            pacientes.setCitasCollection(new ArrayList<Citas>());
        }
        if (pacientes.getTratamientosCollection() == null) {
            pacientes.setTratamientosCollection(new ArrayList<Tratamientos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Citas> attachedCitasCollection = new ArrayList<Citas>();
            for (Citas citasCollectionCitasToAttach : pacientes.getCitasCollection()) {
                citasCollectionCitasToAttach = em.getReference(citasCollectionCitasToAttach.getClass(), citasCollectionCitasToAttach.getCitNumero());
                attachedCitasCollection.add(citasCollectionCitasToAttach);
            }
            pacientes.setCitasCollection(attachedCitasCollection);
            Collection<Tratamientos> attachedTratamientosCollection = new ArrayList<Tratamientos>();
            for (Tratamientos tratamientosCollectionTratamientosToAttach : pacientes.getTratamientosCollection()) {
                tratamientosCollectionTratamientosToAttach = em.getReference(tratamientosCollectionTratamientosToAttach.getClass(), tratamientosCollectionTratamientosToAttach.getTraNumero());
                attachedTratamientosCollection.add(tratamientosCollectionTratamientosToAttach);
            }
            pacientes.setTratamientosCollection(attachedTratamientosCollection);
            em.persist(pacientes);
            for (Citas citasCollectionCitas : pacientes.getCitasCollection()) {
                Pacientes oldCitPacienteOfCitasCollectionCitas = citasCollectionCitas.getCitPaciente();
                citasCollectionCitas.setCitPaciente(pacientes);
                citasCollectionCitas = em.merge(citasCollectionCitas);
                if (oldCitPacienteOfCitasCollectionCitas != null) {
                    oldCitPacienteOfCitasCollectionCitas.getCitasCollection().remove(citasCollectionCitas);
                    oldCitPacienteOfCitasCollectionCitas = em.merge(oldCitPacienteOfCitasCollectionCitas);
                }
            }
            for (Tratamientos tratamientosCollectionTratamientos : pacientes.getTratamientosCollection()) {
                Pacientes oldTraPacienteOfTratamientosCollectionTratamientos = tratamientosCollectionTratamientos.getTraPaciente();
                tratamientosCollectionTratamientos.setTraPaciente(pacientes);
                tratamientosCollectionTratamientos = em.merge(tratamientosCollectionTratamientos);
                if (oldTraPacienteOfTratamientosCollectionTratamientos != null) {
                    oldTraPacienteOfTratamientosCollectionTratamientos.getTratamientosCollection().remove(tratamientosCollectionTratamientos);
                    oldTraPacienteOfTratamientosCollectionTratamientos = em.merge(oldTraPacienteOfTratamientosCollectionTratamientos);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPacientes(pacientes.getPacIdentificacion()) != null) {
                throw new PreexistingEntityException("Pacientes " + pacientes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pacientes pacientes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pacientes persistentPacientes = em.find(Pacientes.class, pacientes.getPacIdentificacion());
            Collection<Citas> citasCollectionOld = persistentPacientes.getCitasCollection();
            Collection<Citas> citasCollectionNew = pacientes.getCitasCollection();
            Collection<Tratamientos> tratamientosCollectionOld = persistentPacientes.getTratamientosCollection();
            Collection<Tratamientos> tratamientosCollectionNew = pacientes.getTratamientosCollection();
            List<String> illegalOrphanMessages = null;
            for (Citas citasCollectionOldCitas : citasCollectionOld) {
                if (!citasCollectionNew.contains(citasCollectionOldCitas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Citas " + citasCollectionOldCitas + " since its citPaciente field is not nullable.");
                }
            }
            for (Tratamientos tratamientosCollectionOldTratamientos : tratamientosCollectionOld) {
                if (!tratamientosCollectionNew.contains(tratamientosCollectionOldTratamientos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tratamientos " + tratamientosCollectionOldTratamientos + " since its traPaciente field is not nullable.");
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
            pacientes.setCitasCollection(citasCollectionNew);
            Collection<Tratamientos> attachedTratamientosCollectionNew = new ArrayList<Tratamientos>();
            for (Tratamientos tratamientosCollectionNewTratamientosToAttach : tratamientosCollectionNew) {
                tratamientosCollectionNewTratamientosToAttach = em.getReference(tratamientosCollectionNewTratamientosToAttach.getClass(), tratamientosCollectionNewTratamientosToAttach.getTraNumero());
                attachedTratamientosCollectionNew.add(tratamientosCollectionNewTratamientosToAttach);
            }
            tratamientosCollectionNew = attachedTratamientosCollectionNew;
            pacientes.setTratamientosCollection(tratamientosCollectionNew);
            pacientes = em.merge(pacientes);
            for (Citas citasCollectionNewCitas : citasCollectionNew) {
                if (!citasCollectionOld.contains(citasCollectionNewCitas)) {
                    Pacientes oldCitPacienteOfCitasCollectionNewCitas = citasCollectionNewCitas.getCitPaciente();
                    citasCollectionNewCitas.setCitPaciente(pacientes);
                    citasCollectionNewCitas = em.merge(citasCollectionNewCitas);
                    if (oldCitPacienteOfCitasCollectionNewCitas != null && !oldCitPacienteOfCitasCollectionNewCitas.equals(pacientes)) {
                        oldCitPacienteOfCitasCollectionNewCitas.getCitasCollection().remove(citasCollectionNewCitas);
                        oldCitPacienteOfCitasCollectionNewCitas = em.merge(oldCitPacienteOfCitasCollectionNewCitas);
                    }
                }
            }
            for (Tratamientos tratamientosCollectionNewTratamientos : tratamientosCollectionNew) {
                if (!tratamientosCollectionOld.contains(tratamientosCollectionNewTratamientos)) {
                    Pacientes oldTraPacienteOfTratamientosCollectionNewTratamientos = tratamientosCollectionNewTratamientos.getTraPaciente();
                    tratamientosCollectionNewTratamientos.setTraPaciente(pacientes);
                    tratamientosCollectionNewTratamientos = em.merge(tratamientosCollectionNewTratamientos);
                    if (oldTraPacienteOfTratamientosCollectionNewTratamientos != null && !oldTraPacienteOfTratamientosCollectionNewTratamientos.equals(pacientes)) {
                        oldTraPacienteOfTratamientosCollectionNewTratamientos.getTratamientosCollection().remove(tratamientosCollectionNewTratamientos);
                        oldTraPacienteOfTratamientosCollectionNewTratamientos = em.merge(oldTraPacienteOfTratamientosCollectionNewTratamientos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pacientes.getPacIdentificacion();
                if (findPacientes(id) == null) {
                    throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.");
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
            Pacientes pacientes;
            try {
                pacientes = em.getReference(Pacientes.class, id);
                pacientes.getPacIdentificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Citas> citasCollectionOrphanCheck = pacientes.getCitasCollection();
            for (Citas citasCollectionOrphanCheckCitas : citasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pacientes (" + pacientes + ") cannot be destroyed since the Citas " + citasCollectionOrphanCheckCitas + " in its citasCollection field has a non-nullable citPaciente field.");
            }
            Collection<Tratamientos> tratamientosCollectionOrphanCheck = pacientes.getTratamientosCollection();
            for (Tratamientos tratamientosCollectionOrphanCheckTratamientos : tratamientosCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pacientes (" + pacientes + ") cannot be destroyed since the Tratamientos " + tratamientosCollectionOrphanCheckTratamientos + " in its tratamientosCollection field has a non-nullable traPaciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pacientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pacientes> findPacientesEntities() {
        return findPacientesEntities(true, -1, -1);
    }

    public List<Pacientes> findPacientesEntities(int maxResults, int firstResult) {
        return findPacientesEntities(false, maxResults, firstResult);
    }

    private List<Pacientes> findPacientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pacientes.class));
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

    public Pacientes findPacientes(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pacientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pacientes> rt = cq.from(Pacientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
