package com.example.TicketingSystem.controllers;

import com.example.TicketingSystem.models.TicketDepartment;
import com.example.TicketingSystem.repositories.TicketDepartmentRepository;
import com.example.TicketingSystem.services.TicketDepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/department")
@Tag(name = "Department Management", description = "APIs for managing departments")  // Grouping APIs under "Department Management"
public class TicketDepartmentController {

    @Autowired
    private TicketDepartmentService ticketDepartmentService;

    @Autowired
    private TicketDepartmentRepository ticketDepartmentRepository;

    // ✅ Get all departments (Fetch departments)
    @Operation(summary = "Get all departments", description = "Retrieve a list of all available departments.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departments retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No departments found")
    })
    @GetMapping
    public ResponseEntity<List<TicketDepartment>> getAllDepartments() {
        List<TicketDepartment> departments = ticketDepartmentService.getAllDepartments();
        if (departments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departments);
    }


    // ✅ Get department by email (Fetch
    //
    // department using email)
    @Operation(summary = "Get department by email", description = "Retrieve the department details for a given email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Department not found for the given email")
    })
    @GetMapping("/{email}")
    public ResponseEntity<TicketDepartment> getDepartmentByEmail(
            @Parameter(description = "Email of the user to find the department") @PathVariable String email) {
        Optional<TicketDepartment> department = ticketDepartmentRepository.findByEmailId(email);
        return department.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ Save a user by department (Save user to department)
    @Operation(summary = "Save a user to a department", description = "Assign a user to a specific department.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully saved to department"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public ResponseEntity<TicketDepartment> saveUserByDepartment( @RequestBody TicketDepartment ticketDepartment) {
        TicketDepartment savedUser = ticketDepartmentService.saveUserToDepartment(ticketDepartment);
        return ResponseEntity.ok(savedUser);
    }

    // ✅ Update user status
    @Operation(summary = "Update user status", description = "Update the status (active/inactive) of a user in a department.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Missing 'isActive' value in request body"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/status/{id}")
    public ResponseEntity<String> updateUserStatus(
            @Parameter(description = "ID of the user to update") @PathVariable String id,
            @RequestBody Map<String, Boolean> requestBody
    ) {
        Boolean isActive = requestBody.get("isActive");

        if (isActive == null) {
            return ResponseEntity.badRequest().body("Missing 'isActive' value in request body.");
        }

        Optional<TicketDepartment> updated = ticketDepartmentService.updateUserStatus(id, isActive);

        if (updated.isPresent()) {
            return ResponseEntity.ok("User status updated successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
