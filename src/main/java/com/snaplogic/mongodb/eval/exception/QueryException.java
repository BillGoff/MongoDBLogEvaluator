package com.snaplogic.mongodb.eval.exception;

/**
 * This class is the QueryException class.  It would get created in the event we could not build a query based on
 * the input parameters supplied.
 * @author bgoff
 * @since 20 Feb 2015
 *
 */
public class QueryException extends RuntimeException
{
	private static final long serialVersionUID = -6574119837403378843L;

	/**
	 * (U) Constructs a new QueryException with null as its details.
	 */
	public QueryException()
	{}
	
	/**
	 * (U) Constructs a new QueryException with the specified detail message.
	 *
	 * @param message String value to set the message to.
	 */
	public QueryException(String message)
	{
		super(message);
	}
	
	/**
	 * (U) Constructs a new QueryException with the specified detail message and cause.
	 *
	 * @param message String value to set the message to.
	 * @param cause   Throwable class to set the cause to.
	 */
	public QueryException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	/**
	 * (U) Constructs a new QueryException with the specified detail message, cause, suppression flag
	 * set to either enabled or disabled, and the writable stack trace flag set to either enable or
	 * disabled.
	 *
	 * @param message             String value to set the message to.
	 * @param cause               Throwable class to set the cause to.
	 * @param enableSuppression   boolean used to set the enabled suppression flag to.
	 * @param writeableStackTrace boolean used to set the write able stack trace flag to.
	 */
	public QueryException(String message, Throwable cause, boolean enableSuppression,
			boolean writeableStackTrace)
	{
		super(message, cause, enableSuppression, writeableStackTrace);
	}
	
	/**
	 * (U) Constructs a new QueryException with the cause set.
	 * 
	 * @param cause Throwable class to set the cause to.
	 */
	public QueryException(Throwable cause)
	{
		super(cause);
	}
}