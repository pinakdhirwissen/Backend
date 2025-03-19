package com.example.TicketingSystem.repositories;

import com.example.TicketingSystem.models.TicketDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketDepartmentRepository extends JpaRepository<TicketDepartment, String> {

    // ✅ Find users by department name
    List<TicketDepartment> findByDepartment(String department);

    // ✅ Find users by active/inactive status (field isActive from entity)
    List<TicketDepartment> findByIsActive(Boolean isActive);
}
