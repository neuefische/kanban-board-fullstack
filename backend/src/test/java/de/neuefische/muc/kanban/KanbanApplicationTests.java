package de.neuefische.muc.kanban;

import de.neuefische.muc.kanban.user.LoginData;
import de.neuefische.muc.kanban.user.LoginResponse;
import de.neuefische.muc.kanban.user.UserCreationData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KanbanApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void integrationTest() {
		ResponseEntity<Void> userCreationResponse = restTemplate.postForEntity("/api/users", new UserCreationData("user", "pw", "pw"), Void.class);
		Assertions.assertThat(userCreationResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		ResponseEntity<LoginResponse> loginResponse = restTemplate.postForEntity("/api/auth/login", new LoginData("user", "pw"), LoginResponse.class);
		String jwt = loginResponse.getBody().getJwt();

		ResponseEntity<Task[]> emptyResponse = restTemplate.exchange(
				"/api/kanban",
				HttpMethod.GET,
				new HttpEntity<>(createHeaders(jwt)),
				Task[].class
		);
		Assertions.assertThat(emptyResponse.getBody()).isEmpty();

		ResponseEntity<Void> createResponse = restTemplate.exchange(
				"/api/kanban",
				HttpMethod.POST,
				new HttpEntity<>(new Task("Einkaufen", "Alles", TaskStatus.OPEN), createHeaders(jwt)),
				Void.class
		);
		Assertions.assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		ResponseEntity<Task[]> listResponse = restTemplate.exchange(
				"/api/kanban",
				HttpMethod.GET,
				new HttpEntity<>(createHeaders(jwt)),
				Task[].class
		);
		Assertions.assertThat(listResponse.getBody()).hasSize(1);
		Task task = listResponse.getBody()[0];
		Assertions.assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);

		ResponseEntity<Void> promoteResponse = restTemplate.exchange(
				"/api/kanban/next",
				HttpMethod.PUT, new HttpEntity<>(task, createHeaders(jwt)),
				Void.class
		);
		Assertions.assertThat(promoteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		ResponseEntity<Task> getResponse = restTemplate.exchange(
				"/api/kanban/" + task.getId(),
				HttpMethod.GET,
				new HttpEntity<>(createHeaders(jwt)),
				Task.class
		);
		Assertions.assertThat(getResponse.getBody().getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
	}

	private HttpHeaders createHeaders(String jwt) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + jwt);
		return headers;
	}

}
