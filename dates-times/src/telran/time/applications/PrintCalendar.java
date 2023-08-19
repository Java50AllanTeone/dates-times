package telran.time.applications;

import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.Locale;

public class PrintCalendar {
	
	private static final int TITLE_OFFSET = 10;
	private static final int WEEK_DAYS_OFFSET = 2;
	private static final int COLUMN_WIDTH = 4;
	private static DayOfWeek[] weekDays = DayOfWeek.values();
	private static Locale LOCALE = Locale.getDefault();

	public static void main(String[] args) {
		try {
			RecordArguments recordArguments = getRecordArguments(args);
			printCalendar(recordArguments);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void printCalendar(RecordArguments recordArguments) {
		printTitle(recordArguments.month(), recordArguments.year(), recordArguments.firstDay());
		printWeekDays();
		printDays(recordArguments.month(), recordArguments.year());	
	}

	private static void printDays(int month, int year) {
		int nDays = getMonthDays(month, year);
		int currentWeekDay = getFirstMonthWeekDay(month, year);
//		System.out.println(currentWeekDay);

		System.out.printf("%s", " ".repeat(getFirstColumnOffset(currentWeekDay)));
		for (int day = 1; day <= nDays; day++) {
			System.out.printf("%4d", day);
			
			if (currentWeekDay == 7) {
				currentWeekDay = 0;
				System.out.println();
			}
			currentWeekDay++;
		}
	}

	private static int getFirstColumnOffset(int currentWeekDay) {
		return COLUMN_WIDTH * (currentWeekDay - 1);
//		return COLUMN_WIDTH * currentWeekDay;
	}

	private static int getFirstMonthWeekDay(int month, int year) {
		LocalDate ld = LocalDate.of(year, month, 1);
		int firstMonthWeekDay = ld.get(ChronoField.DAY_OF_WEEK);
		int actualWeekDay = 0;
		
		for (int index = 0; index < weekDays.length; index++) {
			if (weekDays[index].getValue() == firstMonthWeekDay) {
				actualWeekDay = index + 1;
//				actualWeekDay = index;
			}	
		}
		return actualWeekDay;	
	}

	
	private static int getMonthDays(int month, int year) {
		YearMonth ym = YearMonth.of(year, month);
		return ym.lengthOfMonth();
	}

	private static void printWeekDays() {
		System.out.printf("%s", " ".repeat(WEEK_DAYS_OFFSET));
		for (DayOfWeek dayWeek : weekDays) {
			System.out.printf("%s ", dayWeek.getDisplayName(TextStyle.SHORT, LOCALE));
		}
		System.out.println();
	}

	private static void printTitle(int month, int year, DayOfWeek firstDay) {
		Month monthEn = Month.of(month);
		System.out.printf("%s%s %d\n", " ".repeat(TITLE_OFFSET), monthEn.getDisplayName(TextStyle.FULL, LOCALE), year);
		
	}

	private static RecordArguments getRecordArguments(String[] args) throws Exception {	
		int month = getMonthArg(args);
		int year = getYearArg(args);
		DayOfWeek dayOfWeek = getFirstDayOfWeek(args);
		return new RecordArguments(month, year, dayOfWeek);
	}

	private static DayOfWeek getFirstDayOfWeek(String[] args) throws Exception {
		DayOfWeek dayRes = DayOfWeek.MONDAY;
		
		if (args.length > 2) {
			try {
				dayRes = DayOfWeek.valueOf(args[2].toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new Exception("Day of week is not valid");
			}
			
			setFirstWeekDay(dayRes);
		}
		return dayRes;
	}

	
	private static void setFirstWeekDay(DayOfWeek firstDay) {
		int dayIndex = firstDay.getValue() - 1;
	
		reverseArr(weekDays, 0, dayIndex - 1);
		reverseArr(weekDays, dayIndex, weekDays.length - 1);
		reverseArr(weekDays, 0, weekDays.length - 1);
	}

	
	private static void reverseArr(DayOfWeek[] arr, int start, int end) {
		while (start < end) {
	        var temp = arr[start];
	        arr[start] = arr[end];
	        arr[end] = temp;
	        start++;
	        end--;
	    }	
	}

	private static int getYearArg(String[] args) throws Exception {
		int yearRes = LocalDate.now().getYear();
		
		if (args.length > 1) {
			try {
				yearRes = Integer.parseInt(args[1]);
				
				if (yearRes < 0) {
					throw new Exception("Year must not be less than 0");
				}
			} catch (NumberFormatException e) {
				throw new Exception("Year must be a number");
			}
		}
		return yearRes;
	}

	private static int getMonthArg(String[] args) throws Exception {
		int monthRes = LocalDate.now().getMonthValue();
		
		if (args.length > 0) {
			try {
				monthRes = Integer.parseInt(args[0]);
				
				if (monthRes < 1) {
					throw new Exception("Month value must not be less than 1");
				}
				if (monthRes > 12) {
					throw new Exception("Month value must not be greater than 12");
				}
			} catch (NumberFormatException e) {
				throw new Exception("Month value must be a number");
			}
		}
		return monthRes;
	}

}
