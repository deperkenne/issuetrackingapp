package de.perceverance.ke.tcho.fo.assignment2.issuetracking.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A helper utility to read and write from streams.
 * 
 * @author Simon Harrer, Joerg Lenhard
 * @version 1.0
 */
public class ConsoleHelper {

	/**
	 * Creates a {@link ConsoleHelper} using System.in and System.out for their
	 * streams.
	 * 
	 * This is a factory method.
	 * 
	 * Usage
	 * 
	 * <code>
	 * ConsoleHelper console = ConsoleHelper.build();
	 * </code>
	 * 
	 * @return the configured {@link ConsoleHelper}
	 */
	public static ConsoleHelper build() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return new ConsoleHelper(reader, System.out);
	}

	private BufferedReader in;
	private PrintStream out;

	public ConsoleHelper(BufferedReader in, PrintStream out) {
		super();
		this.in = in;
		this.out = out;
	}

	public BufferedReader getIn() {
		return in;
	}

	public PrintStream getOut() {
		return out;
	}

	/**
	 * Ask the user to enter an integer.
	 * 
	 * Usage
	 * 
	 * <code>
	 * int number = askInteger("Please enter a number: ");
	 * // number is an integer
	 * </code>
	 * 
	 * @param message
	 *            the message asking the user to enter an integer
	 * @return the entered integer
	 * @throws IOException
	 */
	public int askInteger(String message) throws IOException {
		while (true) {
			out.println(message);
			String line = readLine();
			try {
				return Integer.parseInt(line);
			} catch (NumberFormatException e) {
				out.format("ERROR: Entered line (%s) is no integer.%n", line);
			}
		}
	}

	/**
	 * Ask the user to enter an integer.
	 * 
	 * Only integers within the range between from and to (including both from and
	 * to values, too) are allowed.
	 * 
	 * Usage
	 * 
	 * <code>
	 * int number = askIntegerInRange("Please enter a number: ", 1, 3);
	 * // number is either 1, 2 or 3
	 * </code>
	 * 
	 * @param message
	 *            the message asking the user to enter an integer
	 * @param from
	 *            lower bound of range of integers which are allowed
	 * @param to
	 *            upper bound of range of integers which are allowed
	 * @return the entered integer which is in between from and to
	 * @throws IOException
	 *             if an error occurs during reading from or writing to a stream
	 */
	public int askIntegerInRange(String message, int from, int to) throws IOException {
		return askIntegerInList(message, convertRangeToList(from, to));
	}

	private List<Integer> convertRangeToList(int from, int to) {
		List<Integer> rangeValues = new LinkedList<>();
		for (int i = from; i <= to; i++) {
			rangeValues.add(i);
		}
		return rangeValues;
	}

	/**
	 * Ask the user to enter an integer.
	 * 
	 * Only integers in the passed list of correct values (
	 * <code>correctValues</code>)are allowed.
	 * 
	 * Usage
	 * 
	 * <code>
	 * List<Integer> numbers = new LinkedList<>();
	 * numbers.add(1);
	 * numbers.add(2);
	 * int number = askIntegerInList("Please enter a number: ", numbers);
	 * // number is either 1 or 2
	 * </code>
	 * 
	 * @param message
	 *            the message asking the user to enter an integer by providing a
	 *            list of integers which are correct
	 * @param correctValues
	 *            list of possible outcome values
	 * @return the entered integer which is included in <code>correctValues</code>
	 * @throws IOException
	 *             if an error occurs during reading from or writing to a stream
	 */
	public int askIntegerInList(String message, List<Integer> correctValues) throws IOException {
		String correctValuesDescription = Arrays.toString(correctValues.toArray());

		while (true) {
			int enteredValue = askInteger(message + "\nAllowed: " + correctValuesDescription);

			if (!correctValues.contains(enteredValue)) {
				out.format("ERROR: Entered integer (%d) is not allowed.%n", enteredValue);
			} else {
				return enteredValue;
			}
		}
	}

	/**
	 * Ask the user to enter a string
	 * 
	 * <code>
	 * String name = askString("Enter a name: ");
	 * // name may be empty
	 * </code>
	 * 
	 * @param message
	 *            the message describing the semantics of the requested input
	 * @return the entered string
	 * @throws IOException
	 *             if an error occurs during reading from or writing to a stream
	 */
	public String askString(String message) throws IOException {
		out.println(message);

		return readLine();
	}

	/**
	 * Ask the user to enter a non-empty string
	 * 
	 * Usage:
	 * 
	 * <code>
	 * String name = askNonEmptyString("Enter a name: ");
	 * // string is not empty
	 * </code>
	 * 
	 * @param message
	 *            the message describing the semantics of the requested input
	 * @return the entered non-empty string
	 * @throws IOException
	 *             if an error occurs during reading from or writing to a stream
	 */
	public String askNonEmptyString(String message) throws IOException {
		while (true) {
			String line = askString(message + "\nString must not be empty!");
			if (line.isEmpty()) {
				out.println("ERROR: Given string is empty!");
			} else {
				return line;
			}
		}
	}

	private String readLine() throws IOException {
		String line = in.readLine();
		if (line == null) {
			throw new IOException("input stream seems to be closed");
		}
		return line.trim();
	}
}
