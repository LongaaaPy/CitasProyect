/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author SENA
 */
@Entity
@Table(name = "pacientes")
@NamedQueries({
    @NamedQuery(name = "Pacientes.findAll", query = "SELECT p FROM Pacientes p")})
public class Pacientes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "PacIdentificacion")
    private String pacIdentificacion;
    @Basic(optional = false)
    @Column(name = "PacNombre")
    private String pacNombre;
    @Column(name = "PacApellidos")
    private String pacApellidos;
    @Basic(optional = false)
    @Column(name = "PacFechaNacimiento")
    @Temporal(TemporalType.DATE)
    private Date pacFechaNacimiento;
    @Basic(optional = false)
    @Column(name = "PacSexo")
    private String pacSexo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "citPaciente")
    private Collection<Citas> citasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "traPaciente")
    private Collection<Tratamientos> tratamientosCollection;

    public Pacientes() {
    }

    public Pacientes(String pacIdentificacion) {
        this.pacIdentificacion = pacIdentificacion;
    }

    public Pacientes(String pacIdentificacion, String pacNombres, Date pacFechaNacimiento, String pacSexo) {
        this.pacIdentificacion = pacIdentificacion;
        this.pacNombre = pacNombre;
        this.pacFechaNacimiento = pacFechaNacimiento;
        this.pacSexo = pacSexo;
    }

    public String getPacIdentificacion() {
        return pacIdentificacion;
    }

    public void setPacIdentificacion(String pacIdentificacion) {
        this.pacIdentificacion = pacIdentificacion;
    }

    public String getPacNombres() {
        return pacNombre;
    }

    public void setPacNombre(String pacNombre) {
        this.pacNombre = pacNombre;
    }

    public String getPacApellidos() {
        return pacApellidos;
    }

    public void setPacApellidos(String pacApellidos) {
        this.pacApellidos = pacApellidos;
    }

    public Date getPacFechaNacimiento() {
        return pacFechaNacimiento;
    }

    public void setPacFechaNacimiento(Date pacFechaNacimiento) {
        this.pacFechaNacimiento = pacFechaNacimiento;
    }

    public String getPacSexo() {
        return pacSexo;
    }

    public void setPacSexo(String pacSexo) {
        this.pacSexo = pacSexo;
    }

    public Collection<Citas> getCitasCollection() {
        return citasCollection;
    }

    public void setCitasCollection(Collection<Citas> citasCollection) {
        this.citasCollection = citasCollection;
    }

    public Collection<Tratamientos> getTratamientosCollection() {
        return tratamientosCollection;
    }

    public void setTratamientosCollection(Collection<Tratamientos> tratamientosCollection) {
        this.tratamientosCollection = tratamientosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pacIdentificacion != null ? pacIdentificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pacientes)) {
            return false;
        }
        Pacientes other = (Pacientes) object;
        if ((this.pacIdentificacion == null && other.pacIdentificacion != null) || (this.pacIdentificacion != null && !this.pacIdentificacion.equals(other.pacIdentificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modelo.Pacientes[ pacIdentificacion=" + pacIdentificacion + " ]";
    }
    
}
