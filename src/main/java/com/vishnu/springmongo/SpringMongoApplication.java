package com.vishnu.springmongo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vishnu.springmongo.documents.Commit;
import com.vishnu.springmongo.repository.CommitRepository;

@SpringBootApplication
public class SpringMongoApplication implements CommandLineRunner {
	
	@Autowired
	private CommitRepository commitRepository;

	public static void main(String[] args) throws IOException {
		SpringApplication.run(SpringMongoApplication.class, args);
		}

	@Override
	public void run(String... args) throws Exception {
		List<Commit> commits = new ArrayList<>();
		String url = "https://api.bitbucket.org/2.0/repositories/vishnubaghel91/spring-mongo/commits";
		RestTemplate restClient = new RestTemplate();
		HttpHeaders httpHeaders = createHeaders("vishnubaghel91", "Tech@2019");
		HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
		
		ResponseEntity<String> responseEntity  = restClient.exchange(url, HttpMethod.GET, entity, String.class);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(responseEntity.getBody());
		
		JsonNode values = root.get("values");
		
	
		if (values.isArray()) {
			for (JsonNode value : values) {
				String revisionNumber = value.get("hash").asText();
				JsonNode authorNode = value.get("author");
				String authorNameNEmail = authorNode.get("raw").asText();
				
				Commit commit = new Commit();
				commit.setRevisionNumber(revisionNumber);
				commit.setAuthor(authorNameNEmail);
				commits.add(commit);
			}
		}
 		
		List<Commit> newCommits = new ArrayList<>();
		
		//---
		
		for (Commit commit : commits)
		{
			List<Commit> dbcommits = commitRepository.findByRevisionNumber(commit.getRevisionNumber());
			if (dbcommits.isEmpty()) {
				newCommits.add(commit);
			}
		}
		
		//---
		
		if (!newCommits.isEmpty()) {
			commitRepository.saveAll(newCommits);
		}
	}
	
	

	
	private HttpHeaders createHeaders(final String userId, final String password) {
		String auth = userId + ":" + password;
		byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(StandardCharsets.US_ASCII));
		String authHeader = "Basic " + new String(encodedAuth);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authHeader);
		return headers;
	}
}
