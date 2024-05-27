package dev.erichaag.mightycon.core.conference;

import java.time.LocalDate;

public record ConferenceUpdate(
        String name,
        LocalDate startsOn,
        LocalDate endsOn,
        String location
) {
}
