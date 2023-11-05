import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

public class DistanceCalculatorTest {

    public static void main(String[] args) {
        String apiKey = "DEIN_GOOGLE_MAPS_API_SCHLÜSSEL";
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();

        String origin = "Minden Amselstraße 4";
        String destination = "Bielefeld Eckendorferstraße 35";

        try {
            DirectionsApiRequest request = DirectionsApi.getDirections(context, origin, destination)
                    .mode(TravelMode.DRIVING) // Reisemodus (DRIVING, WALKING, BICYCLING, TRANSIT)
                    .optimizeWaypoints(true) // Optionale Optimierung der Zwischenstopps
                    .region("de"); // Region für die Routenberechnung (optional)

            DirectionsResult result = request.await();

            if (result.routes.length > 0) {
                // Die Entfernung in Metern
                long distanceInMeters = result.routes[0].legs[0].distance.inMeters;
                System.out.println("Entfernung zwischen " + origin + " und " + destination + ": " + distanceInMeters + " Meter");
            } else {
                System.out.println("Keine Route gefunden.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}