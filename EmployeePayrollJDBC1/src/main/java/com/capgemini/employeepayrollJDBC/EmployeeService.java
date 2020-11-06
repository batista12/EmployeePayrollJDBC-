package com.capgemini.employeepayrollJDBC;

import java.sql.*;
import java.util.*;

import java.sql.Statement;
import java.time.LocalDate;

public class EmployeeService {
	public List<EmployeeData> viewEmployeePayroll() throws DBException {
		List<EmployeeData> employeePayrollList = new ArrayList<>();
		EmployeePayrollJDBC obj = new EmployeePayrollJDBC();
		EmployeeData empDataObj = null;
		String query = "select * from Employee_Payroll";
		try (Connection con = obj.getConnection()) {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				double salary = resultSet.getDouble(3);
				LocalDate start = resultSet.getDate(4).toLocalDate();
				empDataObj = new EmployeeData(id, name, salary, start);
				employeePayrollList.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollList;
	}
}
