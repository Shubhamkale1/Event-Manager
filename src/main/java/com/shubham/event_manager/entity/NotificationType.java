package com.shubham.event_manager.entity;

public enum NotificationType {

    // Event related
    EVENT_CANCELLED,
    EVENT_UPDATED,
    EVENT_REMINDER,
    EVENT_COMPLETED,

    // Registration related
    REGISTRATION_CONFIRMED,
    REGISTRATION_CANCELLED,

    // Organization related
    NEW_FOLLOWER,
    NEW_EVENT_FROM_FOLLOWED_ORG,

    // Account related
    ROLE_CHANGED,
    WELCOME
}