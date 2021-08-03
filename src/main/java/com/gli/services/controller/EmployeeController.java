package com.gli.services.controller;

import com.gli.services.dao.EmployeeRepository;
import com.gli.services.model.Employee;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@Controller
@RequestMapping("/")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping(value="employee")
    public @ResponseBody Iterable<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @GetMapping(value="employee/{id}")
    public @ResponseBody Employee getEmployeeById(@PathVariable(name="id") int id) {
        return employeeRepository.findById(id);
    }

    @DeleteMapping(value = "employee/{id}")
    public @ResponseBody String deleteEmployeeById(@PathVariable(name="id") int id) {
        employeeRepository.deleteById(id);
        return "Success -> Deleted";
    }

    @PutMapping(value = "employee")
    public @ResponseBody String addEmployee(@RequestBody String body) {
        if (body == null || body.trim().equals("")) {
            return "Body is empty";
        }

        try {
            JSONObject jsonBody = new JSONObject(body);
            if (!jsonBody.has("full_name")) {
                return "Full name is mandatory";
            } else if (!jsonBody.has("address")) {
                return "Address is mandatory";
            } else if (!jsonBody.has("dob")) {
                return "Dob is mandatory";
            } else if (!jsonBody.has("role_id")) {
                return "Role Id is mandatory";
            } else if (!jsonBody.has("salary")) {
                return "Salary is mandatory";
            } else if (!jsonBody.has("role_name")) {
                return "Role name is mandatory";
            }

            Employee employee = new Employee();
            employee.setName(jsonBody.getString("full_name"));
            employee.setAddress(jsonBody.getString("address"));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(jsonBody.getString("dob"));
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            employee.setDob(timestamp);

            employee.setRole(jsonBody.getInt("role_id"));
            employee.setSalary(jsonBody.getInt("salary"));
            employee.setRoleName(jsonBody.getString("role_name"));

            employeeRepository.save(employee);
            return "Success -> Added";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @PostMapping(value = "employee/{id}")
    public @ResponseBody String updateEmployeeById(@PathVariable(name="id") int id, @RequestBody String body) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            Employee employee = employeeRepository.findById(id);

            if (jsonObject.has("full_name")) {
                employee.setName(jsonObject.getString("full_name"));
            }

            if (jsonObject.has("salary")) {
                employee.setSalary(jsonObject.getInt("salary"));
            }

            if (jsonObject.has("dob")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date parsedDate = dateFormat.parse(jsonObject.getString("dob"));
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                employee.setDob(timestamp);
            }

            employeeRepository.save(employee);
            return "Success -> Updated";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}