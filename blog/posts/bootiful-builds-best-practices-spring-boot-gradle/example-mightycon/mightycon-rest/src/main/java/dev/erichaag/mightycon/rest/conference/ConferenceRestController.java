package dev.erichaag.mightycon.rest.conference;

import dev.erichaag.mightycon.core.conference.Conference;
import dev.erichaag.mightycon.core.conference.ConferenceAdd;
import dev.erichaag.mightycon.core.conference.ConferenceService;
import dev.erichaag.mightycon.core.conference.ConferenceUpdate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/conferences")
public class ConferenceRestController {

    private final ConferenceService conferenceService;

    public ConferenceRestController(ConferenceService conferenceService) {
        this.conferenceService = conferenceService;
    }

    @GetMapping
    Collection<Conference> getConferences() {
        return conferenceService.getConferences();
    }

    @GetMapping("/{id}")
    Optional<Conference> getConference(@PathVariable String id) {
        return conferenceService.getConference(UUID.fromString(id));
    }

    @PostMapping
    Conference addConference(@RequestBody ConferenceAdd add) {
        return conferenceService.addConference(add);
    }

    @PatchMapping("/{id}")
    Conference updateConference(@PathVariable String id, @RequestBody ConferenceUpdate update) {
        return conferenceService.updateConference(UUID.fromString(id), update);
    }

}
