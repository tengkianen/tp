package seedu.trippie.command;

import seedu.trippie.data.Place;
import seedu.trippie.Ui;
import seedu.trippie.data.Trip;
import seedu.trippie.data.TripList;

import java.util.List;

public class DeletePlaceCommand extends Command {
    private int placeIndex;

    public DeletePlaceCommand(String userInput) {
        try {
            String index = userInput.split(" /p ")[1];
            placeIndex = Integer.parseInt(index) - 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("It is not found.");
        } catch (NumberFormatException e) {
            System.out.println("It must be number.");
        }
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public void execute(Ui ui, Trip trip, TripList tripList) {
        List<Place> places = trip.getPlaceListObject().getPlaceList();
        if (placeIndex >= 0 && placeIndex < places.size()) {
            System.out.println("Noted. I've removed this place.");
            System.out.println(places.get(placeIndex).toString());
            places.remove(placeIndex);
            System.out.println("There are " + places.size() + " items in the list.");
        } else {
            System.out.println("Enter a valid index.");
        }
        trip.getPlaceListObject().setPlaceList(places);
    }
}
