package com.capgemini.employeepayrollJDBC;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeePayrollJDBCTest {
	static EmployeeService serviceObj;
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
	public void givenUpdatedSalary_WhenRetrieved_ShouldBeSyncedWithDB() throws DBServiceException{
		serviceObj.updateSalary("Ambani", 3000000.00);
		boolean isSynced = serviceObj.check(employeeList, "Ambani", 3000000.00);
		assertTrue(isSynced);
	}

}