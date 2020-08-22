import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Evaluator {
    public static String[] extractAction(String command) throws DukeException {
        String[] split = command.split(" ", 2);
        int len = split.length;

        // command is empty
        if (len == 0) {
            throw new DukeException(Messenger.EMPTY_COMMAND_ERROR);
        }

        String status = split[0];
        if (!(status.equals("todo") ||
                status.equals("done") ||
                status.equals("event") ||
                status.equals("deadline") ||
                status.equals("delete"))) {
            throw new DukeException(Messenger.SPELL_ERROR);
        }

        // no description supplied
        if (len == 1) {
            throw new DukeException(Messenger.emptyDescriptionError(command));
        }
        return split;
    }

    public static String[] extractTime(String command) throws DukeException {
        String[] split = command.split(" /by | /at ", 2);

        // validate if there are two parts
        if (split.length < 2) {
            if (split[0].charAt(0) == '/') {
                throw new DukeException(Messenger.EMPTY_CONTENT_ERROR);
            } else {
                throw new DukeException(Messenger.EMPTY_TIME_ERROR);
            }
        }

        // validate if time is of the correct format
        String time = split[1];
        validateFormat(time);
        return split;
    }

    private static void validateFormat(String value) throws DukeException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate ld = LocalDate.parse(value, formatter);
            String result = ld.format(formatter);
            if (!result.equals(value)) {
                throw new DukeException(Messenger.DATE_FORMAT_ERROR);
            }
        } catch (DateTimeParseException exp) {
            throw new DukeException(Messenger.DATE_FORMAT_ERROR);
        }
    }
}
