package com.capgemini.employeepayrollJDBC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeePayrollJDBCTest {
	static EmployeeService serviceObj;
	private List<EmployeeData> employeeList = new ArrayList<>();
	
	@BeforeClass
	public static void setUp()  {
		serviceObj = new EmployeeService();
	}
	@Test
	public void givenEmpPayrollDB_WhenRetrieved_ShouldMatchEmpCount() throws DBException{
		List<EmployeeData> empPayrollList = serviceObj.viewEmployeePayroll();
		assertEquals(3, empPayrollList.size());
	}
	@Test
	public void givenUpdatedSalary_WhenRetrieved_ShouldBeSyncedWithDB() throws DBException{
		serviceObj.updateSalary("Ambani", 3000000.00);
		boolean isSynced = serviceObj.check(employeeList, "Ambani", 3000000.00);
		assertTrue(isSynced);
	}
	@Test
	public void givenUpdatedSalaryWhenUpdatedUsingPreparedStatementShouldSyncWithDatabase() throws DBException {
		employeeList = serviceObj.viewEmployeePayroll();
		serviceObj.updateSalaryUsingPreparedStatement("Ambani", 2000000.00,
				EmployeeService.statementType.PREPARED_STATEMENT);
		boolean result = serviceObj.check(employeeList, "Ambani", 2000000.00);
		assertTrue(result);
	}
	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchEmpCount() throws DBException{
		List<EmployeeData> empPayrollList = serviceObj.viewEmployeePayrollByJoinDateRange(LocalDate.of(2018,02,01), LocalDate.now() );
		assertEquals(3, empPayrollList.size());
	}

}