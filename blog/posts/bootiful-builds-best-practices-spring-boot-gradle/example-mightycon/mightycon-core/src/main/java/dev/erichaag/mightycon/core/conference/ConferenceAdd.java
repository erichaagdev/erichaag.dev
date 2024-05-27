package dev.erichaag.mightycon.core.conference;

import java.time.LocalDate;

public record ConferenceAdd(
        String name,
        LocalDate startsOn,
        LocalDate endsOn,
        String location
) {
}
