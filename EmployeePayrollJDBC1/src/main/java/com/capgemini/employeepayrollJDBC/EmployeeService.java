package com.capgemini.employeepayrollJDBC;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.LocalDate;

public class EmployeeService {
	private static EmployeeService EmployeeService;
	List<EmployeeData> employeePayrollList;
	EmployeeData empDataObj = null;

	public enum statementType {
		STATEMENT, PREPARED_STATEMENT
	}

	public static EmployeeService getInstance() {
		if (EmployeeService == null)
			EmployeeService = new EmployeeService();
		return EmployeeService;
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
				String salary = resultSet.getString(3);
				LocalDate start = resultSet.getDate(4).toLocalDate();
				String gender = resultSet.getString(5);
				empDataObj = new EmployeeData(emp_id, name, salary, start, gender);
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

	public boolean check(List<EmployeeData> employeeList, String name, String salary) throws DBException {
		EmployeeData employeeObj = getEmployee(employeeList, name);
		employeeObj.setSalary(salary);
		return employeeObj.equals(getEmployee(viewEmployeePayroll(), name));

	}

	EmployeeData getEmployee(List<EmployeeData> employeeList, String name) {
		EmployeeData employee = employeeList.stream()
				.filter(employeeObj -> ((employeeObj.getName()).equals(name))).findFirst().orElse(null);
		return employee;
	}
	public List<EmployeeData> viewEmployeePayrollByJoinDateRange(LocalDate startDate, LocalDate endDate)
			throws DBException {
		List<EmployeeData> employeePayrollListByStartDate = new ArrayList<>();
		String query = "select * from Employee_Payroll where start_date between ? and  ?";
		try (Connection con = new EmployeePayrollJDBC().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setDate(1, Date.valueOf(startDate));
			preparedStatement.setDate(2, Date.valueOf(endDate));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int emp_id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				String salary = resultSet.getString(3);
				LocalDate start = resultSet.getDate(4).toLocalDate();
				String gender = resultSet.getString(5);
				empDataObj = new EmployeeData(emp_id, name, salary, start, gender);
				employeePayrollListByStartDate.add(empDataObj);
			}
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return employeePayrollListByStartDate;
	}
	public Map<String, Double> viewEmployeeDataGroupedByGender(String column, String operation)
			throws DBException {
		Map<String, Double> empDataByGender = new HashMap<>();
		String query = String.format("select gender , %s(%s) from Employee_Payroll group by gender;", operation,
				column);
		try (Connection con = new EmployeePayrollJDBC().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				empDataByGender.put(resultSet.getString(1), resultSet.getDouble(2));
			}
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
		return empDataByGender;
	}
	public void addNewEmployeeToDB(String name, String gender, String salary, LocalDate start_date)
			throws DBException {
		String query = "insert into Employee_Payroll ( name , gender, salary , start_date) values (?,?,?,?)";
		try (Connection con = new EmployeePayrollJDBC().getConnection()) {
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, gender);
			preparedStatement.setString(3, salary);
			preparedStatement.setDate(4, Date.valueOf(start_date));
			preparedStatement.executeUpdate();
			empDataObj = new EmployeeData(name, gender, start_date, salary);
			viewEmployeePayroll().add(empDataObj);
		} catch (Exception e) {
			throw new DBException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}
	public EmployeeData addEmployeeToEmployeeAndPayroll(String name, double salary, String gender,
			LocalDate startDate) throws DBException {
		int emp_id = -1;
		Connection connection = null;
		EmployeeData EmployeeData = null;
		connection = new EmployeePayrollJDBC().getConnection();
		try (Statement statement = connection.createStatement()) {
			String sql = String.format(
					"INSERT INTO employee_payroll(name,gender,salary,startDate) VALUES ('%s','%s','%s','%s')", name,
					startDate, salary);
			int rowAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					emp_id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try (Statement statement = connection.createStatement()) {
			double deductions = salary * 0.2;
			double taxablePay = salary - deductions;
			double tax = taxablePay * 0.1;
			double netPay = salary - tax;
			String sql = String.format(
					"INSERT INTO payroll_details(employee_id,basic_pay,deductions,taxable_pay,tax,net_pay)VALUES (%s,%s,%s,%s,%s,%s)",
					emp_id, salary, deductions, taxablePay, tax, netPay);
			int rowAffected = statement.executeUpdate(sql);
			if (rowAffected == 1) {
				EmployeeData = new EmployeeData(emp_id, name, gender,startDate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return EmployeeData;
	}
	public void removeEmployeeFromDB(int empId) throws DBException{
		String query = String.format("update Employee_Payroll set is_active = false WHERE id= '%s';",empId);
		try(Connection connection=EmployeePayrollJDBC.getConnection()){
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.executeUpdate();
		}catch (SQLException e) {
			throw new DBException("SQL Exception", DBServiceExceptionType.SQL_EXCEPTION);
		}
	}
}
