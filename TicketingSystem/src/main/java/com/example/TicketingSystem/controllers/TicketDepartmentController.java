package com.example.TicketingSystem.controllers;

import com.example.TicketingSystem.models.TicketDepartment;
import com.example.TicketingSystem.repositories.TicketDepartmentRepository;
import com.example.TicketingSystem.services.TicketDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/department")
public class TicketDepartmentController {

    @Autowired
    private TicketDepartmentService ticketDepartmentService;
    @Autowired
    private TicketDepartmentRepository ticketDepartmentRepository;

    // ✅ Get all departments (Fetch departments)
    @GetMapping
    public ResponseEntity<List<TicketDepartment>> getAllDepartments() {
        List<TicketDepartment> departments = ticketDepartmentService.getAllDepartments();
        if (departments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departments);
    }
    // ✅ Get users by department (Fetch users by department)
    @GetMapping("/by-department")
    public ResponseEntity<List<TicketDepartment>> getUsersByDepartment(@RequestParam String department) {
        List<TicketDepartment> users = ticketDepartmentService.getUsersByDepartment(department);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{email}")
    public TicketDepartment getDepartmentByEmail(@PathVariable String email){
        return ticketDepartmentRepository.findByEmailId(email)
                .orElseThrow(()-> new RuntimeException(("Department not found for email: "+email)));
    }

    // ✅ Save a user by department (SaveByDepartment)
    @PostMapping
    public ResponseEntity<TicketDepartment> saveUserByDepartment(@RequestBody TicketDepartment ticketDepartment) {
        TicketDepartment savedUser = ticketDepartmentService.saveUserToDepartment(ticketDepartment);
        return ResponseEntity.ok(savedUser);
    }
    @PutMapping("/status/{email}")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable String id,
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