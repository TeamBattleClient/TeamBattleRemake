package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException {
	/** Instance of CrashReport. */
	private final CrashReport theReportedExceptionCrashReport;

	public ReportedException(CrashReport p_i1356_1_) {
		theReportedExceptionCrashReport = p_i1356_1_;
	}

	@Override
	public Throwable getCause() {
		return theReportedExceptionCrashReport.getCrashCause();
	}

	/**
	 * Gets the CrashReport wrapped by this exception.
	 */
	public CrashReport getCrashReport() {
		return theReportedExceptionCrashReport;
	}

	@Override
	public String getMessage() {
		return theReportedExceptionCrashReport.getDescription();
	}
}
