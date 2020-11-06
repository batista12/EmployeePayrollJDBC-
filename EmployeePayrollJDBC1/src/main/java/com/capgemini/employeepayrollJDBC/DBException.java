package com.capgemini.employeepayrollJDBC;
public class DBException extends Exception{
	public static final DBServiceExceptionType SQL_EXCEPTION = null;
	DBServiceExceptionType exceptionType;
	public DBException(String message, DBServiceExceptionType exceptionType) {
		super(message);
		this.exceptionType=exceptionType;
	}
}
enum DBServiceExceptionType{
	SQL_EXCEPTION , CLASSNOTFOUNDEXCEPTION
}