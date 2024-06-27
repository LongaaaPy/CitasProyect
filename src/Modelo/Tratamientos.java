/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author SENA
 */
@Entity
@Table(name = "tratamientos")
@NamedQueries({
    @NamedQuery(name = "Tratamientos.findAll", query = "SELECT t FROM Tratamientos t")})
public class Tratamientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TraNumero")
    private Integer traNumero;
    @Basic(optional = false)
    @Column(name = "TraFechaAsignado")
    @Temporal(TemporalType.DATE)
    private Date traFechaAsignado;
    @Basic(optional = false)
    @Lob
    @Column(name = "TraDescripcion")
    private String traDescripcion;
    @Basic(optional = false)
    @Column(name = "TraFechaInicio")
    @Temporal(TemporalType.DATE)
    private Date traFechaInicio;
    @Basic(optional = false)
    @Column(name = "TraFechaFin")
    @Temporal(TemporalType.DATE)
    private Date traFechaFin;
    @Basic(optional = false)
    @Lob
    @Column(name = "TraObservaciones")
    private String traObservaciones;
    @JoinColumn(name = "TraPaciente", referencedColumnName = "PacIdentificacion")
    @ManyToOne(optional = false)
    private Pacientes traPaciente;

    public Tratamientos() {
    }

    public Tratamientos(Integer traNumero) {
        this.traNumero = traNumero;
    }

    public Tratamientos(Integer traNumero, Date traFechaAsignado, String traDescripcion, Date traFechaInicio, Date traFechaFin, String traObservaciones) {
        this.traNumero = traNumero;
        this.traFechaAsignado = traFechaAsignado;
        this.traDescripcion = traDescripcion;
        this.traFechaInicio = traFechaInicio;
        this.traFechaFin = traFechaFin;
        this.traObservaciones = traObservaciones;
    }

    public Integer getTraNumero() {
        return traNumero;
    }

    public void setTraNumero(Integer traNumero) {
        this.traNumero = traNumero;
    }

    public Date getTraFechaAsignado() {
        return traFechaAsignado;
    }

    public void setTraFechaAsignado(Date traFechaAsignado) {
        this.traFechaAsignado = traFechaAsignado;
    }

    public String getTraDescripcion() {
        return traDescripcion;
    }

    public void setTraDescripcion(String traDescripcion) {
        this.traDescripcion = traDescripcion;
    }

    public Date getTraFechaInicio() {
        return traFechaInicio;
    }

    public void setTraFechaInicio(Date traFechaInicio) {
        this.traFechaInicio = traFechaInicio;
    }

    public Date getTraFechaFin() {
        return traFechaFin;
    }

    public void setTraFechaFin(Date traFechaFin) {
        this.traFechaFin = traFechaFin;
    }

    public String getTraObservaciones() {
        return traObservaciones;
    }

    public void setTraObservaciones(String traObservaciones) {
        this.traObservaciones = traObservaciones;
    }

    public Pacientes getTraPaciente() {
        return traPaciente;
    }

    public void setTraPaciente(Pacientes traPaciente) {
        this.traPaciente = traPaciente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (traNumero != null ? traNumero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tratamientos)) {
            return false;
        }
        Tratamientos other = (Tratamientos) object;
        if ((this.traNumero == null && other.traNumero != null) || (this.traNumero != null && !this.traNumero.equals(other.traNumero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Tratamientos[ traNumero=" + traNumero + " ]";
    }
    
}
