package com.pizzara.app;

import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

public class OpenRouteServiceExample {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();

        JSONObject requestJson = new JSONObject();
        JSONArray locations = new JSONArray();
        JSONArray point1 = new JSONArray().put(9.70093).put(48.477473);
        JSONArray point2 = new JSONArray().put(9.207916).put(49.153868);
        JSONArray point3 = new JSONArray().put(37.573242).put(55.801281);
        JSONArray point4 = new JSONArray().put(115.663757).put(38.106467);
        locations.put(point1).put(point2).put(point3).put(point4);

        requestJson.put("locations", locations);
        requestJson.put("metrics", new JSONArray().put("distance").put("duration"));
        requestJson.put("resolve_locations", "true");

        String payload = requestJson.toString();

        Entity<String> entity = Entity.json(payload);

        Response response = client.target("https://api.openrouteservice.org/v2/matrix/driving-car")
                .request()
                .header("Authorization", "5b3ce3597851110001cf62485fa24b95ed1b4de7b3f3315f080bc346")
                .header("Accept", "application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8")
                .header("Content-Type", "application/json; charset=utf-8")
                .post(entity);

        if (response.getStatus() == 200) {
            String responseBody = response.readEntity(String.class);
            System.out.println("OpenRouteService Response: " + responseBody);

            JSONObject jsonResponse = new JSONObject(responseBody);

            if (jsonResponse.has("durations") && jsonResponse.has("distances")) {
                JSONArray durations = jsonResponse.getJSONArray("durations");
                JSONArray distances = jsonResponse.getJSONArray("distances");

                createTable(durations, distances);
            } else {
                System.out.println("Fehler in der OpenRouteService-Antwort: Daten nicht vorhanden");
            }
        } else {
            System.out.println("Fehler beim Aufrufen des OpenRouteService: " + response.getStatus());
        }
    }

    private static void createTable(JSONArray durations, JSONArray distances) {
        int rows = durations.length();
        int cols = durations.getJSONArray(0).length();

        Object[][] data = new Object[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double distanceValue = distances.getJSONArray(i).getDouble(j);
                double durationValue = durations.getJSONArray(i).getDouble(j);

                String distanceString = String.format("Distanz: %.2f Meter", distanceValue);
                String durationString = String.format("Dauer: %.2f Sekunden", durationValue);

                data[i][j] = distanceString + " | " + durationString;
            }
        }

        Object[] columnNames = new Object[cols];
        for (int j = 0; j < cols; j++) {
            columnNames[j] = "Punkt " + (j + 1);
        }

        JTable table = new JTable(data, columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        table.setFont(new Font("Arial", Font.PLAIN, 14));

        for (int j = 0; j < cols; j++) {
            table.getColumnModel().getColumn(j).setPreferredWidth(350);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("OpenRouteService Ergebnisse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(800, 400);
        frame.setVisible(true);
    }
}