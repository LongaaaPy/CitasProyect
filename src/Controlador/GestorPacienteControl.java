/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.ConsPacienteInternalFrame;
import java.awt.event.*;
import java.util.LinkedList;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.table.DefaultTableModel;
import java.util.*;

/**
 *
 * @author Sluiooktue
 */

public class GestorPacienteControl implements ActionListener 
{
     Controlador.PacientesJpaController pacientesModelo;
     Vista.ConsPacienteInternalFrame consultarPacienteVista;
    
      public GestorPacienteControl(Vista.ConsPacienteInternalFrame consultarPacienteVista){
        this.consultarPacienteVista=consultarPacienteVista;
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("CitasProyect2PU");
        pacientesModelo=new Controlador.PacientesJpaController(emf);
     }
 
 public void actionPerformed(ActionEvent e){
 
 
     DefaultTableModel tmodelo;
     String valor=consultarPacienteVista.txt_valor.getText();
     int parametro=0;
 
     if(consultarPacienteVista.rdb_identificacion.isSelected()){ 
     parametro=1;
     }
     if(consultarPacienteVista.rdb_nombres.isSelected()){ 
     parametro=2;
     }
     if(consultarPacienteVista.rdb_apellidos.isSelected()){ 
     parametro=3;
     }
     if(consultarPacienteVista.rdb_genero.isSelected()){ 
     parametro=4;
     }
     Vector<Modelo.Pacientes> pacientes = new Vector<>(pacientesModelo.findPacientesEntities());
     String registro[] = new String[5];
    String[] Titulos = {"Identificacion", "Nombre", "Apellidos", "Fecha Nacimiento", "Genero"};

 
    
 
     tmodelo=new DefaultTableModel();
     tmodelo.setColumnIdentifiers(Titulos);
 
 for(Modelo.Pacientes p:pacientes){
      registro[0]=p.getPacIdentificacion();
      registro[1]=p.getPacNombres();
      registro[2]=p.getPacApellidos();
      registro[3]=p.getPacFechaNacimiento().toString();
      registro[4]=(String) p.getPacSexo();
      tmodelo.addRow(registro);
 }
 
 consultarPacienteVista.Tbl_datos.setModel(tmodelo);
 } 
}