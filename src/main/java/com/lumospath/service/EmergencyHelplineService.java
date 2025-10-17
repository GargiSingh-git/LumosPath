package com.lumospath.service;

import com.lumospath.model.EmergencyContact;
import com.lumospath.model.EmergencyContact.ContactType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing emergency helplines and mental health support contacts
 */
public class EmergencyHelplineService {
    private List<EmergencyContact> emergencyContacts;

    public EmergencyHelplineService() {
        this.emergencyContacts = new ArrayList<>();
        initializeHelplines();
    }

    /**
     * Initialize helpline database with Indian mental health support numbers
     */
    private void initializeHelplines() {
        // National helplines
        addContact("Vandrevala Foundation Helpline", "1860-266-2345", "India", 
                  ContactType.SUICIDE_PREVENTION, "24x7 suicide prevention helpline", true);
        
        addContact("AASRA", "022-27546669", "India", 
                  ContactType.SUICIDE_PREVENTION, "Suicide prevention helpline", false);
        
        addContact("Sneha Foundation", "044-24640050", "India", 
                  ContactType.CRISIS_COUNSELING, "Crisis intervention and suicide prevention", true);
        
        addContact("iCall Helpline", "022-25563291", "India", 
                  ContactType.MENTAL_HEALTH, "Psychosocial helpline by TISS", false);
        
        addContact("Sumaitri", "011-23389090", "Delhi", 
                  ContactType.CRISIS_COUNSELING, "Befriending service for emotional support", false);
        
        addContact("Roshni Helpline", "040-66202000", "Hyderabad", 
                  ContactType.SUICIDE_PREVENTION, "Suicide prevention and counseling", true);

        // Regional helplines
        addContact("Sahai", "080-25497777", "Bangalore", 
                  ContactType.MENTAL_HEALTH, "24-hour helpline for emotional support", true);
        
        addContact("Maithri Helpline", "0484-2540530", "Kerala", 
                  ContactType.SUICIDE_PREVENTION, "Suicide prevention helpline", true);
        
        addContact("Lifeline Foundation", "033-24637401", "Kolkata", 
                  ContactType.CRISIS_COUNSELING, "Crisis intervention support", false);
        
        addContact("Jeevan Aastha Helpline", "022-27755495", "Mumbai", 
                  ContactType.MENTAL_HEALTH, "Mental health support", false);

        // Specialized helplines
        addContact("Mann Talks", "8686-139-139", "India", 
                  ContactType.MENTAL_HEALTH, "Mental health helpline by Mann Talks", false);
        
        addContact("Kiran Mental Health Helpline", "1800-599-0019", "India", 
                  ContactType.MENTAL_HEALTH, "24x7 mental health support by Ministry of Social Justice", true);
        
        addContact("Fortis Stress Helpline", "8376-804-102", "India", 
                  ContactType.STRESS_MANAGEMENT, "Stress and anxiety management", false);

        // Student-specific helplines
        addContact("Student Helpline", "022-26985050", "India", 
                  ContactType.MENTAL_HEALTH, "Mental health support for students", false);
        
        addContact("YourDOST", "www.yourdost.com", "India", 
                  ContactType.MENTAL_HEALTH, "Online counseling platform for students and professionals", true);
    }

    /**
     * Add a contact to the helpline database
     */
    private void addContact(String name, String phoneNumber, String location, 
                           ContactType type, String description, boolean available24x7) {
        EmergencyContact contact = new EmergencyContact(name, phoneNumber, location, type, description);
        contact.setAvailable24x7(available24x7);
        emergencyContacts.add(contact);
    }

    /**
     * Display all emergency helplines
     */
    public void displayAllHelplines() {
        System.out.println("\n🚨 Emergency Mental Health Helplines 🚨");
        System.out.println("=" .repeat(60));
        System.out.println("⚠️  If you're having thoughts of self-harm, please reach out immediately!");
        System.out.println("=" .repeat(60));

        // Group by availability
        List<EmergencyContact> available24x7 = emergencyContacts.stream()
                .filter(EmergencyContact::isAvailable24x7)
                .collect(Collectors.toList());
        
        List<EmergencyContact> regularHours = emergencyContacts.stream()
                .filter(contact -> !contact.isAvailable24x7())
                .collect(Collectors.toList());

        // Display 24x7 helplines first
        if (!available24x7.isEmpty()) {
            System.out.println("\n🕐 24x7 Available Helplines:");
            System.out.println("-" .repeat(40));
            for (EmergencyContact contact : available24x7) {
                displayContact(contact);
            }
        }

        // Display regular hours helplines
        if (!regularHours.isEmpty()) {
            System.out.println("\n🕘 Regular Hours Helplines:");
            System.out.println("-" .repeat(40));
            for (EmergencyContact contact : regularHours) {
                displayContact(contact);
            }
        }

        System.out.println("\n💙 Remember: You are not alone. These professionals are trained to help.");
        System.out.println("🌟 Your life has value and meaning. Reach out - people care about you!");
    }

    /**
     * Display helplines by location
     */
    public void displayHelplinesByLocation(String location) {
        List<EmergencyContact> locationContacts = emergencyContacts.stream()
                .filter(contact -> contact.getLocation().equalsIgnoreCase(location) || 
                                 contact.getLocation().equalsIgnoreCase("India"))
                .collect(Collectors.toList());

        if (locationContacts.isEmpty()) {
            System.out.println("\n🔍 No specific helplines found for " + location + ".");
            System.out.println("Here are national helplines that can help:");
            displayHelplinesByLocation("India");
            return;
        }

        System.out.println("\n📍 Mental Health Helplines for " + location + ":");
        System.out.println("=" .repeat(50));
        
        for (EmergencyContact contact : locationContacts) {
            displayContact(contact);
        }
    }

    /**
     * Display helplines by type
     */
    public void displayHelplinesByType(ContactType type) {
        List<EmergencyContact> typeContacts = emergencyContacts.stream()
                .filter(contact -> contact.getType() == type)
                .collect(Collectors.toList());

        if (typeContacts.isEmpty()) {
            System.out.println("\n❌ No helplines found for " + type.getDisplayName());
            return;
        }

        System.out.println("\n🎯 " + type.getDisplayName() + " Helplines:");
        System.out.println("=" .repeat(50));
        
        for (EmergencyContact contact : typeContacts) {
            displayContact(contact);
        }
    }

    /**
     * Display urgent helplines for crisis situations
     */
    public void displayCrisisHelplines() {
        System.out.println("\n🚨 CRISIS HELPLINES - IMMEDIATE SUPPORT 🚨");
        System.out.println("=" .repeat(60));
        System.out.println("⚠️  If this is an emergency, please call these numbers immediately!");
        System.out.println("=" .repeat(60));

        List<EmergencyContact> crisisContacts = emergencyContacts.stream()
                .filter(contact -> contact.getType() == ContactType.SUICIDE_PREVENTION || 
                                 contact.getType() == ContactType.CRISIS_COUNSELING)
                .filter(EmergencyContact::isAvailable24x7)
                .collect(Collectors.toList());

        if (crisisContacts.isEmpty()) {
            // Fallback to any crisis contacts
            crisisContacts = emergencyContacts.stream()
                    .filter(contact -> contact.getType() == ContactType.SUICIDE_PREVENTION || 
                                     contact.getType() == ContactType.CRISIS_COUNSELING)
                    .collect(Collectors.toList());
        }

        for (EmergencyContact contact : crisisContacts) {
            System.out.println("\n🔴 " + contact.getName().toUpperCase());
            System.out.println("   📞 " + contact.getPhoneNumber());
            System.out.println("   📍 " + contact.getLocation());
            System.out.println("   ℹ️  " + contact.getDescription());
            if (contact.isAvailable24x7()) {
                System.out.println("   ⏰ Available 24x7");
            }
            System.out.println();
        }

        System.out.println("💝 Please remember: You matter. Your life is valuable. Help is available.");
        System.out.println("🤗 These trained professionals want to support you through this difficult time.");
    }

    /**
     * Display a single contact in formatted way
     */
    private void displayContact(EmergencyContact contact) {
        System.out.println("\n• " + contact.getName());
        System.out.println("  📞 " + contact.getPhoneNumber());
        System.out.println("  📍 " + contact.getLocation());
        System.out.println("  💬 " + contact.getDescription());
        if (contact.isAvailable24x7()) {
            System.out.println("  ⏰ Available 24x7");
        }
    }

    /**
     * Interactive helpline menu
     */
    public void showHelplineMenu() {
        System.out.println("\n=== Emergency Helplines & Support ===");
        System.out.println("1. 🚨 Crisis helplines (immediate support)");
        System.out.println("2. 📋 All helplines");
        System.out.println("3. 📍 Helplines by location");
        System.out.println("4. 🎯 Helplines by type");
        System.out.println("5. 🔙 Back to main menu");
        
        System.out.print("\nSelect an option (1-5): ");
    }

    /**
     * Get available locations
     */
    public List<String> getAvailableLocations() {
        return emergencyContacts.stream()
                .map(EmergencyContact::getLocation)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get available contact types
     */
    public List<ContactType> getAvailableContactTypes() {
        return emergencyContacts.stream()
                .map(EmergencyContact::getType)
                .distinct()
                .collect(Collectors.toList());
    }
}