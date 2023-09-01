package com.edhm.productos.api.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edhm.productos.api.entities.Alumno;
import com.edhm.productos.api.services.AlumnoService;

@RestController
public class AlumnoController {
	private static final Logger logger = LoggerFactory.getLogger(AlumnoController.class);

	@Value("${app.storage.path}")
	private String STORAGEPATH;

	@Autowired
	private AlumnoService alumnoService;

	@GetMapping("/alumnos")
	public List<Alumno> alumnos() {
		logger.info("call alumnos");
		List<Alumno> alumnos = alumnoService.findAll();
		logger.info("alumnos: " + alumnos);
		return alumnos;
	}

	@GetMapping("/libros/images/{filename:.+}")
	public ResponseEntity<Resource> files(@PathVariable String filename) throws Exception {
		logger.info("call images: " + filename);
		Path path = Paths.get(STORAGEPATH).resolve(filename);
		logger.info("Path: " + path);
		if (!Files.exists(path)) {
			return ResponseEntity.notFound().build();
		}
		Resource resource = new UrlResource(path.toUri());
		logger.info("Resource: " + resource);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + resource.getFilename() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Paths.get(STORAGEPATH).resolve(filename)))
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(resource.contentLength())).body(resource);
	}
	
	@PostMapping("/alumnos")
	public Alumno crear(@RequestParam(name = "imagen", required = false) MultipartFile imagen,
			@RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido,
			@RequestParam("fechaNacimiento") String fechaNacimiento, @RequestParam("direccion") String direccion,
			@RequestParam("correoElectronico") String correoElectronico,
			@RequestParam("numeroTelefono") String numeroTelefono,
			@RequestParam("promedioCalificaciones") double promedioCalificaciones) throws Exception {
		logger.info("call crear(" + nombre + ", " + apellido + ", " + fechaNacimiento + ", " + direccion + ", "
				+ correoElectronico + ", " + numeroTelefono + "," + promedioCalificaciones + ")");
		Alumno alumno = new Alumno();
		alumno.setNombre(nombre);
		alumno.setApellido(apellido);
		alumno.setFechaNacimiento(fechaNacimiento);
		alumno.setDireccion(direccion);
		alumno.setCorreoElectronico(correoElectronico);
		alumno.setNumeroTelefono(numeroTelefono);
		alumno.setPromedioCalificaciones(promedioCalificaciones);

		if (imagen != null && !imagen.isEmpty()) {
			String filename = System.currentTimeMillis()
					+ imagen.getOriginalFilename().substring(imagen.getOriginalFilename().lastIndexOf("."));
			alumno.setImagen(filename);
			if (Files.notExists(Paths.get(STORAGEPATH))) {
				Files.createDirectories(Paths.get(STORAGEPATH));
			}
			Files.copy(imagen.getInputStream(), Paths.get(STORAGEPATH).resolve(filename));
		}
		alumnoService.save(alumno);
		return alumno;

	}
}
