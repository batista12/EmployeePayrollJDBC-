package com.capgemini.employeepayrollJDBC;

import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EmployeeService {
	List<EmployeeData> employeePayrollList;
	EmployeeData empDataObj = null;

	public enum statementType {
		STATEMENT, PREPARED_STATEMENT
	}

	public List<EmployeeData> viewEmployeePayroll() throws DBException {
		List<EmployeeData> employeePayrollList = new ArrayList<>();
		EmployeePayrollJDBC obj = new EmployeePayrollJDBC();
		String query = "select * from Employee_Payroll";
		try (Connection con = obj.getConnection()) {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				int emp_id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				double salary = resultSet.getDouble(3);
				LocalDate start = resultSet.getDate(4).toLocalDate();
				empDataObj = new EmployeeData(emp_id, name, salary, start);
				employeePayrollList.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollList;
	}

	public int updateSalary(String name, Double salary) {
		String sqlQuery = String.format("UPDATE employee_payroll SET salary = %.2f WHERE NAME = '%s';", salary, name);
		try (Connection connection = EmployeePayrollJDBC.getConnection()) {
			Statement statement = connection.createStatement();
			return statement.executeUpdate(sqlQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateSalaryUsingPreparedStatement(String name, double salary, statementType preparedStatement)
			throws DBException {
		String query = "UPDATE employee_payroll SET salary = ? WHERE name = ?";
		try (Connection con = new EmployeePayrollJDBC().getConnection()) {
			PreparedStatement statement = con.prepareStatement(query);
			statement.setDouble(1, salary);
			statement.setString(2, name);
			return statement.executeUpdate();
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}

	public boolean check(List<EmployeeData> employeeList, String name, double salary) throws DBException {
		EmployeeData employeeObj = getEmployee(employeeList, name);
		employeeObj.setSalary(salary);
		return employeeObj.equals(getEmployee(viewEmployeePayroll(), name));

	}

	private EmployeeData getEmployee(List<EmployeeData> employeeList, String name) {
		EmployeeData employee = employeeList.stream()
				.filter(employeeObj -> ((employeeObj.getName()).equals(name))).findFirst().orElse(null);
		return employee;
	}

}