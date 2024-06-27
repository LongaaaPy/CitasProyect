/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Controlador.exceptions.NonexistentEntityException;
import Modelo.Citas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Modelo.Pacientes;
import Modelo.Medicos;
import Modelo.Consultorios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author SENA
 */
public class CitasJpaController implements Serializable {

    public CitasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Citas citas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pacientes citPaciente = citas.getCitPaciente();
            if (citPaciente != null) {
                citPaciente = em.getReference(citPaciente.getClass(), citPaciente.getPacIdentificacion());
                citas.setCitPaciente(citPaciente);
            }
            Medicos citMedico = citas.getCitMedico();
            if (citMedico != null) {
                citMedico = em.getReference(citMedico.getClass(), citMedico.getMedIdentificacion());
                citas.setCitMedico(citMedico);
            }
            Consultorios citConsultorio = citas.getCitConsultorio();
            if (citConsultorio != null) {
                citConsultorio = em.getReference(citConsultorio.getClass(), citConsultorio.getConNumero());
                citas.setCitConsultorio(citConsultorio);
            }
            em.persist(citas);
            if (citPaciente != null) {
                citPaciente.getCitasCollection().add(citas);
                citPaciente = em.merge(citPaciente);
            }
            if (citMedico != null) {
                citMedico.getCitasCollection().add(citas);
                citMedico = em.merge(citMedico);
            }
            if (citConsultorio != null) {
                citConsultorio.getCitasCollection().add(citas);
                citConsultorio = em.merge(citConsultorio);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Citas citas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Citas persistentCitas = em.find(Citas.class, citas.getCitNumero());
            Pacientes citPacienteOld = persistentCitas.getCitPaciente();
            Pacientes citPacienteNew = citas.getCitPaciente();
            Medicos citMedicoOld = persistentCitas.getCitMedico();
            Medicos citMedicoNew = citas.getCitMedico();
            Consultorios citConsultorioOld = persistentCitas.getCitConsultorio();
            Consultorios citConsultorioNew = citas.getCitConsultorio();
            if (citPacienteNew != null) {
                citPacienteNew = em.getReference(citPacienteNew.getClass(), citPacienteNew.getPacIdentificacion());
                citas.setCitPaciente(citPacienteNew);
            }
            if (citMedicoNew != null) {
                citMedicoNew = em.getReference(citMedicoNew.getClass(), citMedicoNew.getMedIdentificacion());
                citas.setCitMedico(citMedicoNew);
            }
            if (citConsultorioNew != null) {
                citConsultorioNew = em.getReference(citConsultorioNew.getClass(), citConsultorioNew.getConNumero());
                citas.setCitConsultorio(citConsultorioNew);
            }
            citas = em.merge(citas);
            if (citPacienteOld != null && !citPacienteOld.equals(citPacienteNew)) {
                citPacienteOld.getCitasCollection().remove(citas);
                citPacienteOld = em.merge(citPacienteOld);
            }
            if (citPacienteNew != null && !citPacienteNew.equals(citPacienteOld)) {
                citPacienteNew.getCitasCollection().add(citas);
                citPacienteNew = em.merge(citPacienteNew);
            }
            if (citMedicoOld != null && !citMedicoOld.equals(citMedicoNew)) {
                citMedicoOld.getCitasCollection().remove(citas);
                citMedicoOld = em.merge(citMedicoOld);
            }
            if (citMedicoNew != null && !citMedicoNew.equals(citMedicoOld)) {
                citMedicoNew.getCitasCollection().add(citas);
                citMedicoNew = em.merge(citMedicoNew);
            }
            if (citConsultorioOld != null && !citConsultorioOld.equals(citConsultorioNew)) {
                citConsultorioOld.getCitasCollection().remove(citas);
                citConsultorioOld = em.merge(citConsultorioOld);
            }
            if (citConsultorioNew != null && !citConsultorioNew.equals(citConsultorioOld)) {
                citConsultorioNew.getCitasCollection().add(citas);
                citConsultorioNew = em.merge(citConsultorioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = citas.getCitNumero();
                if (findCitas(id) == null) {
                    throw new NonexistentEntityException("The citas with id " + id + " no longer exists.");
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
            Citas citas;
            try {
                citas = em.getReference(Citas.class, id);
                citas.getCitNumero();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The citas with id " + id + " no longer exists.", enfe);
            }
            Pacientes citPaciente = citas.getCitPaciente();
            if (citPaciente != null) {
                citPaciente.getCitasCollection().remove(citas);
                citPaciente = em.merge(citPaciente);
            }
            Medicos citMedico = citas.getCitMedico();
            if (citMedico != null) {
                citMedico.getCitasCollection().remove(citas);
                citMedico = em.merge(citMedico);
            }
            Consultorios citConsultorio = citas.getCitConsultorio();
            if (citConsultorio != null) {
                citConsultorio.getCitasCollection().remove(citas);
                citConsultorio = em.merge(citConsultorio);
            }
            em.remove(citas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Citas> findCitasEntities() {
        return findCitasEntities(true, -1, -1);
    }

    public List<Citas> findCitasEntities(int maxResults, int firstResult) {
        return findCitasEntities(false, maxResults, firstResult);
    }

    private List<Citas> findCitasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Citas.class));
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

    public Citas findCitas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Citas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Citas> rt = cq.from(Citas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
