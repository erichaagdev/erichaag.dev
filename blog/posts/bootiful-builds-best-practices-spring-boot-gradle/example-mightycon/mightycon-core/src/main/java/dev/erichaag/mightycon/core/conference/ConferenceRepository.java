package dev.erichaag.mightycon.core.conference;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ConferenceRepository {

    Collection<Conference> getAll();

    Optional<Conference> get(UUID id);

    void save(Conference conference);

}
