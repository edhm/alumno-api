package com.edhm.productos.api.services;

import java.util.List;

import com.edhm.productos.api.entities.Alumno;

public interface AlumnoService {
	
		public List<Alumno> findAll();

		public Alumno findById(Long id);

		public void save(Alumno alumno);

		public void deleteById(Long id);
	
}
