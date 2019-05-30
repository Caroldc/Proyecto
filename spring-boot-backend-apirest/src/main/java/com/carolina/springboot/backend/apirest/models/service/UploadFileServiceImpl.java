package com.carolina.springboot.backend.apirest.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UploadFileServiceImpl implements IUploadFileService {
	private final String DIRECTORIO_UPLOAD = "uploads";
	private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);

	@Override
	public Resource cargar(String nombreFoto) throws MalformedURLException {

		Path rutaArchivo = getPath(nombreFoto);
		log.info(rutaArchivo.toString());
		// A partir de la ruta que contiene la imagen creamos el recurso
		Resource recurso = new UrlResource(rutaArchivo.toUri());
		// validamos que el recurso exista y se pueda leer
		if (!recurso.exists() && !recurso.isReadable()) {
			rutaArchivo = Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();
			// A partir de la ruta que contiene la imagen creamos el recurso
			recurso = new UrlResource(rutaArchivo.toUri());

			log.error("Error no se pudo cargar la imagen " + nombreFoto);
		}
		return recurso;
	}

	@Override
	public String copiar(MultipartFile archivo) throws IOException {
		// si existe, debemos obtener el nombre original de la imagen. Ahora tendremos
		// que seleccionar una ruta donde almacenar las imagines.Ademas le vamos a
		// concatenar
		// un nombre para que nunca se repitan con UUID.random que genera un
		// identificador random que va a ser siempre unico
		String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
		// indicamos la ruta del archivo,como en este caso esta dentro del proyecto
		// podemos indicarlo así
		// si por el contrario estuviera en una ruta externa C://Temp//uploads
		/*
		 * lo siguiente es resolver el nombre del archivo para concatenar(unir) dentro
		 * del upload y tener una unica ruta
		 */
		Path rutaArchivo = getPath(nombreArchivo);
		log.info(rutaArchivo.toString());
		// Ahora con files.copy copia el archivo que hemos subido al servidor a la ruta
		// escogida
	
			Files.copy(archivo.getInputStream(), rutaArchivo);
		
		return nombreArchivo;
	}

	@Override
	public boolean eliminar(String nombreFoto) {
		if (nombreFoto != null && nombreFoto.length() > 0) {
			// ahora obtenemos la ruta de la foto
			Path rutaFotoAnterior = Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
			// convertimos la imagen en archivo
			File archivoFotoAnterior = rutaFotoAnterior.toFile();
			// si el archivo existe y se puede leer lo borramos
			if (archivoFotoAnterior.exists() && archivoFotoAnterior.canRead()) {
				archivoFotoAnterior.delete();
				return true;

			}

		} 
		return false;
	}

	@Override
	public Path getPath(String nombreFoto) {

		return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
	}

}
