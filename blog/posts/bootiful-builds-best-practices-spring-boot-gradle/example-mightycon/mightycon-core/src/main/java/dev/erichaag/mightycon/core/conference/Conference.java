package dev.erichaag.mightycon.core.conference;

import java.time.LocalDate;
import java.util.UUID;

public record Conference(
        UUID id,
        String name,
        LocalDate startsOn,
        LocalDate endsOn,
        String location
) {
}
