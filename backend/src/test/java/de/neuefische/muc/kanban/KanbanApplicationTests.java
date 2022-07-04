package de.neuefische.muc.kanban;

import de.neuefische.muc.kanban.user.LoginData;
import de.neuefische.muc.kanban.user.LoginResponse;
import de.neuefische.muc.kanban.user.UserCreationData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KanbanApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void integrationTest() {
		ResponseEntity<Void> userCreationResponse = restTemplate.postForEntity("/api/users", new UserCreationData("user", "pw", "pw"), Void.class);
		assertThat(userCreationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity("/api/auth/login", new LoginData("user", "pw"), LoginResponse.class);
		String jwt = loginResponse.getBody().getJwt();

		ResponseEntity<Task[]> emptyResponse = restTemplate.exchange(
				"/api/kanban",
				HttpMethod.GET,
				new HttpEntity<>(createHeaders(jwt)),
				Task[].class
		);
		assertThat(emptyResponse.getBody()).isEmpty();

		jwt = refreshToken(jwt);

		ResponseEntity<Void> createResponse = restTemplate.exchange(
				"/api/kanban",
				HttpMethod.POST,
				new HttpEntity<>(new Task("Einkaufen", "Alles", TaskStatus.OPEN), createHeaders(jwt)),
				Void.class
		);
		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		jwt = refreshToken(jwt);

		ResponseEntity<Task[]> listResponse = restTemplate.exchange(
				"/api/kanban",
				HttpMethod.GET,
				new HttpEntity<>(createHeaders(jwt)),
				Task[].class
		);
		assertThat(listResponse.getBody()).hasSize(1);
		Task task = listResponse.getBody()[0];
		assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);

		jwt = refreshToken(jwt);

		ResponseEntity<Void> promoteResponse = restTemplate.exchange(
				"/api/kanban/next",
				HttpMethod.PUT, new HttpEntity<>(task, createHeaders(jwt)),
				Void.class
		);
		assertThat(promoteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		jwt = refreshToken(jwt);

		ResponseEntity<Task> getResponse = restTemplate.exchange(
				"/api/kanban/" + task.getId(),
				HttpMethod.GET,
				new HttpEntity<>(createHeaders(jwt)),
				Task.class
		);
		assertThat(getResponse.getBody().getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
	}

	private HttpHeaders createHeaders(String jwt) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwt);
		return headers;
	}

	private String refreshToken(String jwt) {
		ResponseEntity<LoginResponse> refreshResponse = restTemplate.exchange(
				"/api/auth/refresh",
				HttpMethod.POST,
				new HttpEntity<>(createHeaders(jwt)),
				LoginResponse.class
		);
		assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		return refreshResponse.getBody().getJwt();
	}

}
