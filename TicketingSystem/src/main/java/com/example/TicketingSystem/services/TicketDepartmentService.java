package com.example.TicketingSystem.services;

import com.example.TicketingSystem.models.TicketDepartment;
import com.example.TicketingSystem.repositories.TicketDepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketDepartmentService {

    @Autowired
    private TicketDepartmentRepository ticketDepartmentRepository;

    // ✅ Fetch all departments (all users in departments)
    public List<TicketDepartment> getAllDepartments() {
        return ticketDepartmentRepository.findAll();
    }

    // ✅ Fetch users by department name
    public List<TicketDepartment> getUsersByDepartment(String department) {
        return ticketDepartmentRepository.findByDepartment(department);
    }

    // ✅ Fetch users by active status (true/false)
    public List<TicketDepartment> getUsersByActiveStatus(Boolean isActive) {
        return ticketDepartmentRepository.findByIsActive(isActive);
    }

    // ✅ Save a new user to a department
    public TicketDepartment saveUserToDepartment(TicketDepartment ticketDepartment) {
        ticketDepartment.setId(UUID.randomUUID().toString()); // generate unique ID
        ticketDepartment.setIsActive(true); // set default status to active on creation
        return ticketDepartmentRepository.save(ticketDepartment);
    }

    // ✅ Update user active/inactive status
    public Optional<TicketDepartment> updateUserStatus(String id, Boolean isActive) {
        Optional<TicketDepartment> departmentUser = ticketDepartmentRepository.findById(id);

        if (departmentUser.isPresent()) {
            TicketDepartment td = departmentUser.get();
            td.setIsActive(isActive); // set active/inactive status
            ticketDepartmentRepository.save(td);
        }

        return departmentUser;
    }
}
