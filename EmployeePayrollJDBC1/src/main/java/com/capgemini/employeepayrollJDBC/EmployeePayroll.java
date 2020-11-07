package com.capgemini.employeepayrollJDBC;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.capgemini.employeepayrollJDBC.EmployeeService.statementType;

public class EmployeePayroll {

	private static EmployeeService EmployeeService;
	private static EmployeeService employeePayrollDBService;
	List<EmployeeData> employeePayrollList = new ArrayList<>();
	PreparedStatement preparedStatement;
	public int employeeId;
	public String name;
	public String salary;
	public LocalDate startDate;

	public EmployeePayroll() {
		employeePayrollDBService = employeePayrollDBService.getInstance();
	}
	public List<EmployeeData> readData() throws DBException {
		employeePayrollList = EmployeeService.viewEmployeePayroll();
		return employeePayrollList;
	}
	public void updateData(String name, double salary, statementType type) throws DBException {
		employeePayrollList = EmployeeService.viewEmployeePayroll();
		int rowAffected = employeePayrollDBService.updateSalary(name, salary);
		if (rowAffected != 0)
			(getEmployeeByName(employeePayrollList, name)).setSalary(salary);
	}

	private EmployeeData getEmployeeByName(List<EmployeeData> employeePayrollList2, String name) {
		EmployeeData employee = employeePayrollList2.stream()
				.filter(employeeObj -> ((employeeObj.getName()).equals(name))).findFirst().orElse(null);
		return employee;
	}

	public boolean checkEmployeeDataInSyncWithDatabase(String name) throws DBException {
		boolean result = false;
		employeePayrollList = employeePayrollDBService.viewEmployeePayroll();
		EmployeeData employee = employeePayrollDBService.getEmployee(employeePayrollList, name);
		result = (getEmployeeByName(employeePayrollList, name)).equals(employee);
		return result;
	}
	public List<EmployeeData> viewEmployeePayrollByJoinDateRange(LocalDate startDate, LocalDate endDate)
			throws DBException {
		return employeePayrollDBService.viewEmployeePayrollByJoinDateRange(startDate, endDate);
	}
	public Map<String, Double> viewEmployeeDataGroupedByGender(String operation, String column)
			throws DBException {
		return employeePayrollDBService.viewEmployeeDataGroupedByGender(operation, column);
	}

}