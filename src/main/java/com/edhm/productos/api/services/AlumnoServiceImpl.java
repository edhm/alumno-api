package com.edhm.productos.api.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edhm.productos.api.entities.Alumno;
import com.edhm.productos.api.repositories.AlumnoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class AlumnoServiceImpl implements AlumnoService {

	@Autowired
	private AlumnoRepository alumnoRepository;

	@Override
	public List<Alumno> findAll() {
		return (List<Alumno>) alumnoRepository.findAll();

	}

	@Override
	public Alumno findById(Long id) {
		return alumnoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No existe registro"));
	}

	@Override
	public void save(Alumno alumno) {
		alumnoRepository.save(alumno);

	}

	@Override
	public void deleteById(Long id) {
		alumnoRepository.deleteById(id);

	}

}
