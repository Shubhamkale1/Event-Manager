package com.shubham.event_manager.service;

import com.shubham.event_manager.dto.DashboardDTO;
import com.shubham.event_manager.dto.EventAnalyticsDTO;
import java.util.Map;

public interface DashboardService {
    DashboardDTO getOrganizerDashboard(
            String userEmail);
    Map<String, Object> getAdminDashboard();
    EventAnalyticsDTO getEventAnalytics(
            Long eventId, String userEmail);
}