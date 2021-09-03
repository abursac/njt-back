package rs.fon.njt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ClassroomReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClassroomReservationApplication.class, args);
	}

}
