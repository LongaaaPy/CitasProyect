/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import Controlador.exceptions.PreexistingEntityException;




/**
 *
 * @author Sluiooktue
 */

public class PacienteControl implements ActionListener 
{
  Vista.RegpacienteInternalFrame pacienteVista;
  Modelo.Pacientes pacienteModelo;
  Controlador.PacientesJpaController gestorPacienteModelo;
  
  public PacienteControl(Vista.RegpacienteInternalFrame pacienteVista)
  {
     this.pacienteVista=pacienteVista;
     EntityManagerFactory emf= Persistence.createEntityManagerFactory("CitasProyect2PU");
     gestorPacienteModelo = new Controlador.PacientesJpaController(emf);
 
 }

    PacienteControl(GestorPacienteControl pacienteVista) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
  
 @Override
 public void actionPerformed(ActionEvent e){
 if(e.getSource().equals(pacienteVista.RegistrarBtn)){
     
   String identificacion=pacienteVista.txt_identificacion.getText();
   String nombres=pacienteVista.txt_nombres.getText();
   String apellidos=pacienteVista.txt_apellidos.getText();
   SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yyyy");
   String fechaNacimiento=formato.format(pacienteVista.Dtd_fecha_nacimiento.getDate());
   String genero=pacienteVista.rdb_masculino.getText();
   String genero_2= pacienteVista.rdb_femenino.getText();
   if(pacienteVista.rdb_masculino.isSelected())
      genero="m";
   else
      genero="f";
   
   pacienteModelo= new Modelo.Pacientes();
   pacienteModelo.setPacIdentificacion(identificacion);
   pacienteModelo.setPacApellidos(apellidos);
   pacienteModelo.setPacNombre(nombres);
   pacienteModelo.setPacFechaNacimiento(new Date(fechaNacimiento));
   pacienteModelo.setPacSexo(genero);
   try
{
gestorPacienteModelo.create(pacienteModelo);
JOptionPane.showMessageDialog(pacienteVista, "Paciente registrado correctamente");
}
catch (PreexistingEntityException ex)
{
JOptionPane.showMessageDialog(pacienteVista, "El paciente ya existe");
}
catch (Exception ex)
{
JOptionPane.showMessageDialog(pacienteVista, ex.getMessage());
}

 }
   
   
 if(e.getSource().equals(pacienteVista.NuevoBtn)){
   pacienteVista.txt_identificacion.setText(null);
   pacienteVista.txt_nombres.setText(null);
   pacienteVista.txt_apellidos.setText(null);
   pacienteVista.Dtd_fecha_nacimiento.setDate(null);
   pacienteVista.rdb_masculino.setSelected(true);
   pacienteVista.rdb_femenino.setSelected(false);
   pacienteVista.txt_identificacion.requestFocus(); 
 }
 }
}