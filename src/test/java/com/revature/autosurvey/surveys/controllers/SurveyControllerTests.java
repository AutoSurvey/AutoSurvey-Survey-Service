package com.revature.autosurvey.surveys.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.revature.autosurvey.surveys.beans.Survey;
import com.revature.autosurvey.surveys.services.SurveyService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class SurveyControllerTests {
	
	@TestConfiguration
	static class Configuration {
		@Bean
		public SurveyController getSurveyController(SurveyService surveyService) {
			SurveyController surveyController = new SurveyController();
			surveyController.setSurveyService(surveyService);
			return surveyController;
		}
		
		@Bean
		public SurveyService getSurveyService() {
			return Mockito.mock(SurveyService.class);
		}
	}
	
	@Autowired
	private SurveyController surveyController;
	
	@Autowired 
	private SurveyService surveyService;
	
	private static UUID validUuid;
	private static UUID invalidUuid;
	
	@BeforeAll
	static void before() {
		invalidUuid = UUID.fromString("5ec294ec-b8d5-11eb-8529-0242ac130003");
		validUuid = UUID.fromString("186d7fd1-1aae-44f4-8755-c3ebb5d4711f");
	}
	
	@Test
	void testGetByUuidRespondsWith404WhenServiceReturnsEmptyMono() {
		doReturn(Mono.empty()).when(surveyService).getByUuid(any());
		
		Mono<ResponseEntity<Object>> result = surveyController.getSurveyByUuid(invalidUuid);
		
		StepVerifier.create(result).expectNext(ResponseEntity.notFound().build()).verifyComplete();;
	}
	
	@Test
	void testGetByUuidRespondsWithSurveyWhenGivenValidUuid() {
		Survey survey = new Survey();
		survey.setUuid(validUuid);
		
		doReturn(Mono.just(survey)).when(surveyService).getByUuid(any());
		Mono<ResponseEntity<Object>> result = surveyController.getSurveyByUuid(validUuid);
		
		StepVerifier.create(result).expectNext(ResponseEntity.ok(survey)).verifyComplete();
	}
	
	@Test
	void testAddSurveyThrowsErrorWhenInvalidJson() {
		
	}
}