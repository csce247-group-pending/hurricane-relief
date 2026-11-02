package com.pending.model.data;

import org.junit.jupiter.api.Test;

class DatabaseTest {


    /**
     * test | reasoning
     *
     * original json files | standard
     * generated isolated sample json with one entry per user | simplified test case, must verify that the users list is of the combined sizes of users, volunteers, and sheltermanagers
     * json files do not exist | proper error handling required
     * json files are improperly formatted | must abort, rather than corrupt program data
     * json files contain the wrong type of data | same as above
     */
    @Test
    void read() {
    }

    /**
     * test | reasoning
     *
     * post fresh instance | should require data to be loaded first
     * after reading corrupt data | should not change the files
     * after reading proper data without making changes | should do nothing
     * after reading proper data and making changes | should write the appropriate data
     */
    @Test
    void write() {
    }

    /**
     * test | reasoning
     *
     * return value is not null | the Database should not be null
     * return value is uniform across multiple calls | the Database should have only one instance
     */
    @Test
    void getInstance() {
    }

    /**
     * test | reasoning
     *
     * "rosa.delgado@example.com", "DelgadoStrong2026!" | standard
     * "Rosa.Delgado@example.com", "DelgadoStrong2026!" | name should be case-insensitive
     * "rosa.delgado@eXaMpLe.com", "DelgadoStrong2026!" | domain should be case-insensitive
     * "rOsA.dElGaDo@example.com", "DelgadoStrong2026!" | name should be case-insensitive
     * "rosa.delgado@Example.com", "DelgadoStrong2026!" | domain should be case-insensitive
     * "rosadelgado@example.com", "DelgadoStrong2026!" | name should ignore periods
     * "rosa.delgado@examplecom", "DelgadoStrong2026!" | domain should not ignore periods
     * "rosa.delgado@example.com", "delgadostrong2026!" | password should be case-sensitive
     * "rosa.delgado@example.com", "" | login should return null with empty password
     * "", "DelgadoStrong2026!" | login should return null with empty email address
     * "rosa.delgado@example.com", null | login should return null with null password
     * null, "DelgadoStrong2026!" | login should return null with null email address
     */
    @Test
    void attemptLogin() {
    }

    /**
     * test | reasoning
     *
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | standard
     * null, "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | first name cannot be null
     * "John", null, 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | last name cannot be null
     * "John", "Smith", null, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | date of birth cannot be null
     * "John", "Smith", 02-28-1999, null, "UIJK(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | relief needs cannot be null
     * etc
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UJ(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | password must be at least 8 characters
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UJK(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | password must be at least 8 characters
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*ix3AAAAAAAAAAA", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | password must be at most 20 characters
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*ix3AAAAAAAAAAAA", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | password must be at most 20 characters
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJKAAix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | password must contain a special character
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "uijk(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | password must contain an uppercase letter
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*ixe", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | password must contain a number
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*IX3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | password must contain a lowercase letter
     * "John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL") | standard
     */
    // Note: email and phone syntax are to be checked by the Contact constructor, not any Database method
    @Test
    void testCreateAccount() {
    }

    /**
     * test | reasoning
     *
     * johnSmith = User("John", "Smith", 02-28-1999, Arrays.toList([MEDICAL_CARE, SHELTER]), "UIJK(*ix3", Location("SC", "Columbia", "300 Main St", 29201), Contact("803-444-4444", "jsmith@outlook.com", "TEXT"), Contact("803-444-4445", "msmith@outlook.com", "CALL"))
     * johnSmith,  | standard
     * null, "need help",
     */
    @Test
    void createReliefRequest() {
    }

    //Note to Claude: use the UUID getter tests from DataList.java here for all the get by UUID methods

    /**
     * test | reasoning
     *
     * User.class | standard
     * ReliefRequest.class | standard
     * Hurricane.class | standard
     * Shelter.class | standard
     * Contact.class | standard
     * Location.class | standard
     * LivingBeing.class | standard
     * Volunteer.class | standard
     * ShelterManager.class | standard
     * String.class | object not in key set
     * null | null check
     */
    @Test
    void get() {
    }

    /**
     * test | reasoning
     *
     * new SkillSort(rosaDelgado) | standard
     * new SkillSort(null) | null check
     * null | null check
     */
    @Test
    void getSortedReliefRequests() {
    }
}