package edu.eci.arsw.treecore.persistenceDAO.impl;

import edu.eci.arsw.treecore.persistence.mappers.ProyectoMapper;
import edu.eci.arsw.treecore.persistenceDAO.ProyectoDAO;
import edu.eci.arsw.treecore.exceptions.PersistenceException;
import edu.eci.arsw.treecore.model.impl.Mensaje;
import edu.eci.arsw.treecore.model.impl.Proyecto;
import edu.eci.arsw.treecore.model.impl.Rama;
import edu.eci.arsw.treecore.model.impl.Usuario;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyBatisProyectoDAO implements ProyectoDAO {

    @Autowired
	private ProyectoMapper proyectoMapper;

    @Override
    public Proyecto getProyecto(int identificador) throws PersistenceException {
        Proyecto proyecto=proyectoMapper.getProyecto(identificador);
		if(proyecto==null) throw new PersistenceException("Proyecto no encontrado");
		else return proyecto;
    }

    @Override
    public ArrayList<Proyecto> getProyectos() throws PersistenceException {
    	ArrayList<Proyecto> projects=this.proyectoMapper.getProyectos();
    	if(projects==null) throw new PersistenceException("Proyectos no encontrados");
		else return projects; 
    }

    @Override
    public ArrayList<Proyecto> getProyectosUsuario(String correo) throws PersistenceException {
        ArrayList<Proyecto> proyectos = proyectoMapper.getProyectosUsuario(correo);
		if(proyectos==null) throw new PersistenceException("El usuario no tiene proyectos");
        else return proyectos;
    }

    @Override
    public ArrayList<Usuario> getParticipantes(int identificador) throws PersistenceException {
        Proyecto proyecto=proyectoMapper.getProyecto(identificador);
		if(proyecto==null) throw new PersistenceException("Proyecto no encontrado");
		else return proyecto.getParticipantes();
    }

    @Override
    public boolean estaParticipando(String correo, int identificador) throws PersistenceException {
        try {
            ArrayList<Usuario> participantes=getParticipantes(identificador);
            boolean esta = false;
            for (int i = 0 ; i < participantes.size(); i++){
                if (correo.equals(participantes.get(i).getCorreo())){
                    esta = true;
                    break;
                }
            }
            return esta;
        }
        catch (PersistenceException e){
            throw(e);
        }
    }

    @Override
    public ArrayList<Rama> getRamas(int identificador) throws PersistenceException {
        Proyecto proyecto=proyectoMapper.getProyecto(identificador);
		if(proyecto==null) throw new PersistenceException("Proyecto no encontrado");
		else return proyecto.getRamas();
    }

    @Override
    public ArrayList<Mensaje> getMensajes(int identificador) throws PersistenceException {
        Proyecto proyecto=proyectoMapper.getProyecto(identificador);
		if(proyecto==null) throw new PersistenceException("Proyecto no encontrado");
		else return proyecto.getMensajes();
    }

    @Override
    public void insertarProyecto (Proyecto proyecto) throws PersistenceException {
        try{
            proyectoMapper.insertarProyecto(proyecto);
            Rama r = new Rama(1, proyecto.getNombre(),  null, null, null, proyecto.getCreador());
            proyectoMapper.insertarRama(r, proyecto);
        }
        catch (Exception e){
            throw new PersistenceException("Error al insertar participante");
        }
    }

    @Override
    public void insertarParticipante (Proyecto proyecto, Usuario usuario) throws PersistenceException {
        try{
            proyectoMapper.insertarParticipante(usuario, proyecto);   
        }
        catch (Exception e){
            throw new PersistenceException("Error al insertar participante");
        }
    }

    @Override
    public void insertarRama (Rama rama, Proyecto proyecto) throws PersistenceException {
        try{
            if (rama.getRamaPadre() != null){
                proyectoMapper.insertarRamaConPadre(rama, proyecto);
            }
            else{
                proyectoMapper.insertarRama(rama, proyecto);
            }    
        }
        catch (Exception e){
            throw new PersistenceException("Error al insertar rama");
        }
    }
}