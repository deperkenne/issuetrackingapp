package de.perceverance.ke.tcho.fo.assignment2.issuetracking.logic;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationHelper {

	private static final String ANY_LETTER_OR_CONSTANT = "[a-zA-Z0-9]";
	private static final String START_WITH_LETTER = "[a-zA-Z]";
	private static final String ZERO_OR_MORE = "*";
	private static final Pattern VALID_ID_REGEX = Pattern
			.compile(START_WITH_LETTER + ANY_LETTER_OR_CONSTANT + ZERO_OR_MORE);

	/**
	 * Validates an ID.
	 * 
	 * An ID has to start with a letter followed by zero or more letters or numbers.
	 * 
	 * @param id
	 *            the id to be checked. must not be null.
	 * @return true if the id is valid, false otherwise
	 * 
	 * @throws NullPointerException
	 *             if id is null
	 */
	public static boolean isId(String id) {
		Objects.requireNonNull(id, "input must not be null");

		Matcher idMatcher = VALID_ID_REGEX.matcher(id);
		return idMatcher.matches();
	}

}
