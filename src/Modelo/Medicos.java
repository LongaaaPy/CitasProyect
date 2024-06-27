/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author SENA
 */
@Entity
@Table(name = "medicos")
@NamedQueries({
    @NamedQuery(name = "Medicos.findAll", query = "SELECT m FROM Medicos m")})
public class Medicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "MedIdentificacion")
    private String medIdentificacion;
    @Basic(optional = false)
    @Column(name = "MedNombres")
    private String medNombres;
    @Basic(optional = false)
    @Column(name = "MedApellidos")
    private String medApellidos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "citMedico")
    private Collection<Citas> citasCollection;

    public Medicos() {
    }

    public Medicos(String medIdentificacion) {
        this.medIdentificacion = medIdentificacion;
    }

    public Medicos(String medIdentificacion, String medNombres, String medApellidos) {
        this.medIdentificacion = medIdentificacion;
        this.medNombres = medNombres;
        this.medApellidos = medApellidos;
    }

    public String getMedIdentificacion() {
        return medIdentificacion;
    }

    public void setMedIdentificacion(String medIdentificacion) {
        this.medIdentificacion = medIdentificacion;
    }

    public String getMedNombres() {
        return medNombres;
    }

    public void setMedNombres(String medNombres) {
        this.medNombres = medNombres;
    }

    public String getMedApellidos() {
        return medApellidos;
    }

    public void setMedApellidos(String medApellidos) {
        this.medApellidos = medApellidos;
    }

    public Collection<Citas> getCitasCollection() {
        return citasCollection;
    }

    public void setCitasCollection(Collection<Citas> citasCollection) {
        this.citasCollection = citasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (medIdentificacion != null ? medIdentificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicos)) {
            return false;
        }
        Medicos other = (Medicos) object;
        if ((this.medIdentificacion == null && other.medIdentificacion != null) || (this.medIdentificacion != null && !this.medIdentificacion.equals(other.medIdentificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Medicos[ medIdentificacion=" + medIdentificacion + " ]";
    }
    
}
