package com.capgemini.employeepayrollJDBC;

import java.sql.Date;
import java.time.LocalDate;

public class EmployeeData {
	private int emp_id;
	private String name;
	private Double salary;
	private LocalDate start_date;
	private String gender;

	public EmployeeData(int emp_id, String name, double salary, LocalDate start, String gender) {
		super();
		this.emp_id = emp_id;
		this.name = name;
		this.salary = salary;
		this.start_date = start;
		this.gender = gender;
	}

	public int getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(int emp_id) {
		this.emp_id = emp_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public LocalDate getStart_date() {
		return start_date;
	}

	public void setStart_date(LocalDate start_date) {
		this.start_date = start_date;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "EmployeeData [emp_id=" + emp_id + ", name=" + name + ", salary=" + salary + ", start_date="
				+ start_date + ", name=" + name + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeData other = (EmployeeData) obj;
		if (emp_id != other.emp_id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (salary == null) {
			if (other.salary != null)
				return false;
		} else if (!salary.equals(other.salary))
			return false;
		if (start_date == null) {
			if (other.start_date != null)
				return false;
		} else if (!start_date.equals(other.start_date))
			return false;
		return true;
	}

}