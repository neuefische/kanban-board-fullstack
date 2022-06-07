package de.neuefische.muc.kanban;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KanbanApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void integrationTest() {
		ResponseEntity<Task[]> emptyResponse = restTemplate.getForEntity("/api/kanban", Task[].class);
		Assertions.assertThat(emptyResponse.getBody()).isEmpty();

		ResponseEntity<Void> createResponse = restTemplate.postForEntity("/api/kanban", new Task("Einkaufen", "Alles", TaskStatus.OPEN), Void.class);
		Assertions.assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		ResponseEntity<Task[]> listResponse = restTemplate.getForEntity("/api/kanban", Task[].class);
		Assertions.assertThat(listResponse.getBody()).hasSize(1);
		Task task = listResponse.getBody()[0];
		Assertions.assertThat(task.getStatus()).isEqualTo(TaskStatus.OPEN);

		ResponseEntity<Void> promoteResponse = restTemplate.exchange("/api/kanban/next", HttpMethod.PUT, new HttpEntity<>(task), Void.class);
		Assertions.assertThat(promoteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

		ResponseEntity<Task> getResponse = restTemplate.getForEntity("/api/kanban/" + task.getId(), Task.class);
		Assertions.assertThat(getResponse.getBody().getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
	}

}
