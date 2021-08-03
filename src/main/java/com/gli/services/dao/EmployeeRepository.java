package com.gli.services.dao;

import com.gli.services.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    @Query(value = "SELECT * FROM employee WHERE id = ?1", nativeQuery = true)
    Employee findById(int id);
}