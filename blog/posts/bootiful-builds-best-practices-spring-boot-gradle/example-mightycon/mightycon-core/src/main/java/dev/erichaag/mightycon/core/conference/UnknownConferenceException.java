package dev.erichaag.mightycon.core.conference;

import java.util.UUID;

class UnknownConferenceException extends RuntimeException {

    private final UUID conferenceId;

    UnknownConferenceException(UUID conferenceId) {
        super("Unknown conference: " + conferenceId);
        this.conferenceId = conferenceId;
    }

    public UUID getConferenceId() {
        return conferenceId;
    }

}
