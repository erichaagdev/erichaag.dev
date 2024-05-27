package dev.erichaag.mightycon;

import dev.erichaag.mightycon.core.conference.ConferenceRepository;
import dev.erichaag.mightycon.core.conference.ConferenceService;
import dev.erichaag.mightycon.core.conference.InMemoryConferenceRepository;
import dev.erichaag.mightycon.rest.conference.ConferenceRestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
class MightyConApplication {

    public static void main(String[] args) {
        SpringApplication.run(MightyConApplication.class, args);
    }

    @Bean
    ConferenceRestController conferenceRestController(ConferenceService conferenceService) {
        return new ConferenceRestController(conferenceService);
    }

    @Bean
    ConferenceService conferenceService(ConferenceRepository conferenceRepository) {
        return new ConferenceService(conferenceRepository);
    }

    @Bean
    ConferenceRepository conferenceRepository() {
        return new InMemoryConferenceRepository();
    }

}
