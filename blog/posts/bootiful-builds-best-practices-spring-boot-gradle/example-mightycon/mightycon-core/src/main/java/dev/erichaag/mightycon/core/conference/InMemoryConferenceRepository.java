package dev.erichaag.mightycon.core.conference;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryConferenceRepository implements ConferenceRepository {

    private static final ConcurrentHashMap<UUID, Conference> conferences = new ConcurrentHashMap<>();

//    Map.of()List.of(
//            new Conference(UUID.randomUUID(), "Gateway Software Symposium", LocalDate.of(2023, 3, 31), LocalDate.of(2023, 4, 1), "St. Louis, Missouri"),
//            new Conference(UUID.randomUUID(), "DPE Summit", LocalDate.of(2023, 9, 20), LocalDate.of(2023, 9, 21), "San Francisco, California"),
//            new Conference(UUID.randomUUID(), "Spring I/O", LocalDate.of(2024, 5, 30), LocalDate.of(2024, 5, 31), "Barcelona, Spain"),
//            new Conference(UUID.randomUUID(), "DPE Summit", LocalDate.of(2024, 9, 24), LocalDate.of(2024, 9, 25), "San Francisco, California")
//            )

    @Override
    public Collection<Conference> getAll() {
        return conferences.values();
    }

    @Override
    public Optional<Conference> get(UUID id) {
        return Optional.ofNullable(conferences.get(id));
    }

    @Override
    public void save(Conference conference) {
        conferences.put(conference.id(), conference);
    }

}
