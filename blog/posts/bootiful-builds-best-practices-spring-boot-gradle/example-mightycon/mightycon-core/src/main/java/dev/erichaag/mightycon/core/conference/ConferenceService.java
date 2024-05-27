package dev.erichaag.mightycon.core.conference;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class ConferenceService {

    private final ConferenceRepository conferenceRepository;

    public ConferenceService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    public Optional<Conference> getConference(UUID id) {
        return conferenceRepository.get(id);
    }

    public Collection<Conference> getConferences() {
        return conferenceRepository.getAll();
    }

    public Conference addConference(ConferenceAdd add) {
        final var newConference = new Conference(
                UUID.randomUUID(),
                add.name(),
                add.startsOn(),
                add.endsOn(),
                add.location()
        );
        conferenceRepository.save(newConference);
        return newConference;
    }

    public Conference updateConference(UUID id, ConferenceUpdate update) {
        final var conference = getConference(id).orElseThrow(() -> new UnknownConferenceException(id));
        final var newConference = new Conference(
                id,
                coalesce(update.name(), conference.name()),
                coalesce(update.startsOn(), conference.startsOn()),
                coalesce(update.endsOn(), conference.endsOn()),
                coalesce(update.location(), conference.location())
        );
        conferenceRepository.save(newConference);
        return newConference;
    }

    private static <T> T coalesce(T first, T second) {
        return first == null ? second : first;
    }

}
