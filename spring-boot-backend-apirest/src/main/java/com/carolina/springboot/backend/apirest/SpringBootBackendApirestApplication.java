package com.carolina.springboot.backend.apirest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/*Esta formada por tres aplicaciones, donde encontramos la configuracion de spring
 * habilitar la autoconfiguraciones, y componentScan, que registra en el contenedor de 
 * spring todas las clases anotadas */
@SpringBootApplication
public class SpringBootBackendApirestApplication {//implements CommandLineRunner {
	//@Autowired
//	private BCryptPasswordEncoder passwordEncoder;
	public static void main(String[] args) {
		SpringApplication.run(SpringBootBackendApirestApplication.class, args);
	}
/* antes de arrancar la aplicacion se va a realizar algo, para eso funciona este metodo de la interfaz
 * CommandLineRunner. En este caso lo que hacemos es generar la password encriptada para poder introducirla en el import.sql*/
	/*@Override
	public void run(String... args) throws Exception {
		String password ="12345";
		for (int i=0; i<4; i++) {
			String passwordBcrypt = passwordEncoder.encode(password);
			System.out.println(passwordBcrypt);
		}
		
	}*/
}
