package com.lumospath.model;

/**
 * EmergencyContact model class for storing helpline information
 */
public class EmergencyContact {
    private int contactId;
    private String name;
    private String phoneNumber;
    private String location; // city, state, or "India" for national
    private String description;
    private ContactType type;
    private boolean available24x7;
    private String website;

    // Constructors
    public EmergencyContact() {}

    public EmergencyContact(String name, String phoneNumber, String location, 
                           ContactType type, String description) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.type = type;
        this.description = description;
        this.available24x7 = false;
    }

    // Getters and Setters
    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContactType getType() {
        return type;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public boolean isAvailable24x7() {
        return available24x7;
    }

    public void setAvailable24x7(boolean available24x7) {
        this.available24x7 = available24x7;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return name + " - " + phoneNumber + " (" + location + ")" +
               (available24x7 ? " [24x7 Available]" : "");
    }

    /**
     * Enum for different types of emergency contacts
     */
    public enum ContactType {
        SUICIDE_PREVENTION("Suicide Prevention"),
        MENTAL_HEALTH("Mental Health Support"),
        CRISIS_COUNSELING("Crisis Counseling"),
        DEPRESSION_SUPPORT("Depression Support"),
        ANXIETY_SUPPORT("Anxiety Support"),
        STRESS_MANAGEMENT("Stress Management"),
        GENERAL_HELPLINE("General Helpline");

        private final String displayName;

        ContactType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}