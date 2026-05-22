package com.shubham.event_manager.controller;

import com.shubham.event_manager.entity.*;
import com.shubham.event_manager.exception.ResourceNotFoundException;
import com.shubham.event_manager.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Export",
        description = "Export attendee lists and analytics")
public class ExportController {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationRepository
            registrationRepository;

    @GetMapping("/{id}/attendees/export")
    @Operation(
            summary = "Export attendee list",
            description = "Downloads CSV or Excel. " +
                    "Organizer or admin only.",
            security = @SecurityRequirement(
                    name = "bearerAuth")
    )
    public ResponseEntity<byte[]> exportAttendees(
            @PathVariable Long id,
            @RequestParam(defaultValue = "csv")
            String format,
            @AuthenticationPrincipal
            UserDetails userDetails)
            throws Exception {

        User requestingUser = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        Event event = eventRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Event not found: " + id));

        List<Registration> registrations =
                registrationRepository
                        .findByEventOrderByRegisteredAtAsc(
                                event)
                        .stream()
                        .filter(r -> r.getStatus()
                                == RegistrationStatus.CONFIRMED)
                        .toList();

        if ("excel".equalsIgnoreCase(format)) {
            return exportAsExcel(event, registrations);
        } else {
            return exportAsCsv(event, registrations);
        }
    }

    private ResponseEntity<byte[]> exportAsCsv(
            Event event,
            List<Registration> registrations)
            throws Exception {

        StringBuilder csv = new StringBuilder();
        csv.append(
                "Name,Email,Registered At,Status\n");

        for (Registration r : registrations) {
            csv.append(r.getUser().getName())
                    .append(",")
                    .append(r.getUser().getEmail())
                    .append(",")
                    .append(r.getRegisteredAt())
                    .append(",")
                    .append(r.getStatus())
                    .append("\n");
        }

        byte[] bytes = csv.toString()
                .getBytes("UTF-8");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + event.getTitle()
                                + "-attendees.csv\"")
                .contentType(MediaType.parseMediaType(
                        "text/csv"))
                .body(bytes);
    }

    private ResponseEntity<byte[]> exportAsExcel(
            Event event,
            List<Registration> registrations)
            throws Exception {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(
                "Attendees");

        // Header row
        Row header = sheet.createRow(0);
        String[] headers = {
                "Name", "Email",
                "Registered At", "Status"};

        CellStyle headerStyle =
                workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data rows
        int rowNum = 1;
        for (Registration r : registrations) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(
                    r.getUser().getName());
            row.createCell(1).setCellValue(
                    r.getUser().getEmail());
            row.createCell(2).setCellValue(
                    r.getRegisteredAt().toString());
            row.createCell(3).setCellValue(
                    r.getStatus().toString());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + event.getTitle()
                                + "-attendees.xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-" +
                                "officedocument.spreadsheetml.sheet"))
                .body(out.toByteArray());
    }
}