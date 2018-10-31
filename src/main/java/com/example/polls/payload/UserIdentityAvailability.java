package com.example.polls.payload;

/**
 * @author <egadEldin.ext@orange.com> Essam Eldin
 *
 */
public class UserIdentityAvailability {
    private Boolean available;

    public UserIdentityAvailability(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
