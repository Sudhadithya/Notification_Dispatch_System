package com.notificationsystem.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<ChannelType> preferences;

    public User() {}

    public User(String id, String name, String email, String phoneNumber, Set<ChannelType> preferences) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;

        // DECISION: Perform a "Defensive Copy" of the preferences set.
        // Why? If we just assign `this.preferences = preferences`, the calling code could
        // modify the Set *after* creating the User, breaking immutability and causing side effects.
        this.preferences = new HashSet<>(preferences);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Set<ChannelType> getPreferences() {
        // DECISION: Return an unmodifiable view of the set.
        // Why? Encapsulation. We don't want outside classes adding/removing preferences 
        // directly from the User object without going through proper business methods.
        return java.util.Collections.unmodifiableSet(preferences);
    }
}
