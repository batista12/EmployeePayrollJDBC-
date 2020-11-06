package com.capgemini.employeepayrollJDBC;
public class DBException extends Exception{
	DBServiceExceptionType exceptionType;
	public DBException(String message, DBServiceExceptionType exceptionType) {
		super(message);
		this.exceptionType=exceptionType;
	}
}
enum DBServiceExceptionType{
	SQL_EXCEPTION , CLASSNOTFOUNDEXCEPTION
}