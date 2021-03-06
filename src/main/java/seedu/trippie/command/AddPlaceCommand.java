package seedu.trippie.command;

import seedu.trippie.Ui;
import seedu.trippie.data.Place;
import seedu.trippie.data.TrippieData;
import seedu.trippie.exception.TrippieInvalidArgumentException;

import java.util.Collections;
import java.util.List;

public class AddPlaceCommand extends Command {

    private static final String FORMAT_ERROR_MESSAGE = "Incorrect format for [add] command! Please try the following:\n"
            + "Format: add /n PLACE_NAME /d DAY /t TIME\n"
            + "Example: add /n Dinner at Marina Bay Sands /d 2 /t 1800 to 2000";
    private static final String PARAMETER_ERROR_MESSAGE = "Please check that your DAY parameter is in the numerical "
            + "form.";
    private static final String TIME_ERROR_MESSAGE = "Please check that your TIME parameters are in the correct "
            + "24-hours format";
    private static final String TIME_TRAVELLER_ERROR_MESSAGE = "Are you a time traveller? Your END TIME should "
            + "end before START TIME";

    private final String name;
    private final int day;
    private final int start;
    private final int end;

    public AddPlaceCommand(String userInput) throws TrippieInvalidArgumentException {
        try {
            this.name = extractName(userInput);
            this.day = extractDay(userInput);
            this.start = extractStartTime(userInput);
            this.end = extractEndTime(userInput);
            char[] characters = userInput.toCharArray();
            if (characters[3] != ' ') {
                throw new TrippieInvalidArgumentException(FORMAT_ERROR_MESSAGE);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new TrippieInvalidArgumentException(FORMAT_ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            throw new TrippieInvalidArgumentException(PARAMETER_ERROR_MESSAGE);
        }
    }

    public String extractName(String userInput) {
        String withoutDay = userInput.split(" /d ")[0];
        return withoutDay.split(" /n ")[1];
    }

    public int extractDay(String userInput) {
        String withoutTime = userInput.split(" /t ")[0];
        String day = withoutTime.split(" /d ")[1];
        return Integer.parseInt(day.trim());
    }

    public int extractStartTime(String userInput) throws TrippieInvalidArgumentException {
        return extractTime(userInput, true);
    }

    private int extractTime(String userInput, boolean isStart) throws TrippieInvalidArgumentException {
        String time = userInput.split(" /t ")[1];
        String start = time.split("to")[0];
        String end = time.split("to")[1];
        int parsedTime;
        if (isStart) {
            parsedTime = Integer.parseInt(start.trim());
        } else {
            parsedTime = Integer.parseInt(end.trim());
        }
        int hours = parsedTime / 100;
        int minutes = parsedTime % 100;
        if (minutes >= 60 || minutes < 0 || hours >= 24 || hours < 0) {
            throw new TrippieInvalidArgumentException(TIME_ERROR_MESSAGE);
        }
        return parsedTime;
    }

    public int extractEndTime(String userInput) throws TrippieInvalidArgumentException {
        int endTime = extractTime(userInput, false);
        if (endTime < this.start) {
            throw new TrippieInvalidArgumentException(TIME_TRAVELLER_ERROR_MESSAGE);
        }
        return endTime;
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public void execute(Ui ui, TrippieData trippieData) {
        List<Place> places = trippieData.getCurrentTrip().getPlaceListObject().getPlaceList();
        Place newPlace = new Place(name, day, start, end);
        places.add(newPlace);
        if (places.size() > 1) {
            sortPlaceList(places);
        }
        System.out.println("Got it. I've added this place:");
        System.out.println(newPlace);
        System.out.printf("Now you have %d %s in the list.%n", places.size(), places.size() > 1 ? "places" : "place");
        int placesSize = places.size();
        assert placesSize > 0 : "placesSize should be greater than 0";
        trippieData.getCurrentTrip().getPlaceListObject().setPlaceList(places);
    }

    public void sortPlaceList(List<Place> sortedPlaces) {
        Boolean swapped = false;
        for (int i = (sortedPlaces.size() - 1); i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (sortedPlaces.get(i).getPlaceDay() == sortedPlaces.get(j).getPlaceDay()
                        && sortedPlaces.get(i).getPlaceStartTime() < sortedPlaces.get(j).getPlaceStartTime()) {
                    Collections.swap(sortedPlaces, i, j);
                    swapped = true;
                } else if (sortedPlaces.get(i).getPlaceDay() < sortedPlaces.get(j).getPlaceDay()) {
                    Collections.swap(sortedPlaces, i, j);
                    swapped = true;
                }
            }
            if (swapped == false) {
                break;
            }
        }
    }
}
