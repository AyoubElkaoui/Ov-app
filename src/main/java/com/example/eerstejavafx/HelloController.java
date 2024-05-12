//Fix layout
//aankomsttijd bus nog verwerken
//vertrektijd/aankomsttijd in grote tabel weergeven ipv geselecteerde tijd
//wisselknop fixen
//vertalingen fixen
//logos bus trein
//plan route knop overbodig?
//

package com.example.eerstejavafx;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.layout.VBox;
import static com.example.eerstejavafx.HelloController.DepartureTimeCalc.calculateDepartureTime;
import javafx.scene.input.MouseEvent;

public class HelloController implements Initializable {

    @FXML
    private Button smallFontSize;

    @FXML
    private Button middleFontSize;

    @FXML
    private Button bigFontSize;

    @FXML
    private Label WijzigLettergrootte;

    @FXML
    private Button refreshPage;

    @FXML
    Button wisselHaltes;

    @FXML
    private Button TaalButton;

    @FXML
    private Label lblVoertuig;
    @FXML
    private Label VertrekLabel;
    @FXML
    private Label huidigeTijd;

    @FXML
    private ChoiceBox<String> VertrekChoiceBox;

    @FXML
    private Label AankomstLabel;

    @FXML
    private ChoiceBox<String> AankomstChoiceBox;

    @FXML
    private TextArea selectedItemTextArea;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private VBox mainContainer;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> timeComboBox;

    private LoginController loginController;

    @FXML
    private Button getRoutesAndTimesButton;

    @FXML
    private ToggleGroup SelectTransportMode;

    @FXML
    private ToggleButton TreinSelector;

    @FXML
    private Button Traject_1;

    @FXML
    private Button Traject_2;

    @FXML
    private Button Traject_3;

    public static int currentPage = 1;

    public void bigFontSize(ActionEvent event) {
        bigFontSizeForLabels(2);
    }

    public void middleFontSize(ActionEvent event) {
        middleFontSizeForLabels(2);
    }

    public void smallFontSize(ActionEvent event) {
        smallFontSizeForLabels(2);
    }

    private void bigFontSizeForLabels(int delta) {
        changeFontSize(4);
    }

    private void middleFontSizeForLabels(int delta) {
        changeFontSize(2);
    }

    private void smallFontSizeForLabels(int delta) {
        changeFontSize(0);
    }

    private final String[] steden = {"Amersfoort", "Amsterdam", "Den Haag", "Eindhoven", "Maastricht", "Utrecht"};

    @FXML
    private ListView<FavoriteRoute> favoritesListView;

    private ObservableList<FavoriteRoute> favoriteRoutes;

    private final Map<String, Map<String, String>> routeTimes = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setValue(LocalDate.now());
        timeComboBox.setValue(updateCurrentTime());
        VertrekChoiceBox.getItems().addAll(steden);
        VertrekChoiceBox.setOnAction(this::getVertrek);
        AankomstChoiceBox.getItems().addAll(steden);
        AankomstChoiceBox.setOnAction(this::getAankomst);

        timeComboBox.getItems().addAll("05:00", "05:15", "05:30", "05:45", "06:00", "06:15", "06:30", "06:45",
                "07:00", "07:15", "07:30", "07:45", "08:00", "08:15", "08:30", "08:45",
                "09:00", "09:15", "09:30", "09:45", "10:00", "10:15", "10:30", "10:45",
                "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45",
                "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45",
                "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45",
                "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30", "18:45",
                "19:00", "19:15", "19:30", "19:45", "20:00", "20:15", "20:30", "20:45",
                "21:00", "21:15", "21:30", "21:45", "22:00", "22:15", "22:30", "22:45",
                "23:00", "23:15", "23:30", "23:45");

        updateCurrentTime();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> updateCurrentTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        routeTimes.put("Amersfoort", Map.of("Eindhoven", "51min", "Utrecht", "12min", "Maastricht", "93min", "Amsterdam", "60min", "Den Haag", "40min"));
        routeTimes.put("Eindhoven", Map.of("Amersfoort", "51min", "Utrecht", "41min", "Maastricht", "42min", "Amsterdam", "50min", "Den Haag", "67min"));
        routeTimes.put("Utrecht", Map.of("Amersfoort", "12min", "Eindhoven", "41min", "Maastricht", "85min", "Amsterdam", "20min", "Den Haag", "29min"));
        routeTimes.put("Maastricht", Map.of("Amersfoort", "90min", "Eindhoven", "42min", "Utrecht", "85min", "Amsterdam", "101min", "Den Haag", "109min"));
        routeTimes.put("Amsterdam", Map.of("Amersfoort", "60min", "Eindhoven", "51min", "Utrecht", "21min", "Maastricht", "101min", "Den Haag", "31min"));
        routeTimes.put("Den Haag", Map.of("Amersfoort", "40min", "Eindhoven", "67min", "Utrecht", "30min", "Maastricht", "109min", "Amsterdam", "31min"));

        getRoutesAndTimesButton.setOnAction(this::getRoutesAndTimes);


        Button bigButton = new Button("groot");
        bigButton.setOnAction(this::bigFontSize);

        Button middleButton = new Button("klein");
        middleButton.setOnAction(this::middleFontSize);

        Button smallButton = new Button("middel");
        smallButton.setOnAction(this::smallFontSize);

        GridPane gridPane = new GridPane();
        gridPane.add(middleButton, 0, 0);
        gridPane.add(smallButton, 1, 0);
        gridPane.add(bigButton, 2, 0);

        favoriteRoutes = FXCollections.observableArrayList();
        favoritesListView.setItems(favoriteRoutes);
    }

    // LETTERGROOTTE WIJZIGEN
    private void changeFontSize(int delta) {
        double defaultSize = 14;
        double currentSize = defaultSize + delta;
        VertrekLabel.setFont(new Font(currentSize));
        AankomstLabel.setFont(new Font(currentSize));
        currentTimeLabel.setFont(new Font(currentSize));
        selectedItemTextArea.setStyle("-fx-font-size: " + currentSize);
    }

    // HUIDIGE TIJD
    private String updateCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String currentTime = dateFormat.format(new Date());
        currentTimeLabel.setText("\uD83D\uDD54 " + currentTime);
        return currentTime;
    }

    // VERTREK VELD
    public void getVertrek(ActionEvent event) {
        String myVertrek = VertrekChoiceBox.getValue();
        updateTextArea(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue());
    }

    // AANKOMST VELD
    public void getAankomst(ActionEvent event) {
        String myAankomst = AankomstChoiceBox.getValue();
        updateTextArea(VertrekChoiceBox.getValue(), myAankomst);
    }

    // REFRESH KNOP
    public void refreshPage(ActionEvent event) {
        VertrekChoiceBox.getSelectionModel().clearSelection();
        AankomstChoiceBox.getSelectionModel().clearSelection();
        selectedItemTextArea.setText("");
        Traject_1.setText("");
        Traject_2.setText("");
        Traject_3.setText("");
        currentPage=1;
    }

    // HALTES WISSELEN
    public void wisselHaltes(ActionEvent event) {
        String vertrek = VertrekChoiceBox.getValue();
        String aankomst = AankomstChoiceBox.getValue();

        if (vertrek != null && aankomst != null) {
            VertrekChoiceBox.setValue(aankomst);
            AankomstChoiceBox.setValue(vertrek);

            updateTextArea(aankomst, vertrek);
            updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 1);
            updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 2);
            updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 3);
        }
    }

    // OV SELECTEREN
    @FXML
    private void handleTransportTypeChange() {
        Toggle selectedToggleButton = SelectTransportMode.getSelectedToggle();
        String voertuig;

        String buttonId = ((ToggleButton) selectedToggleButton).getId();

        // Now you can use the buttonId in your conditional statements
        if ("BusSelector".equals(buttonId)) {
            voertuig = "Bus";
            lblVoertuig.setText(voertuig);
            RouteManager.setTransportationBus();
        } else {
            voertuig = "Trein";
            lblVoertuig.setText(voertuig);
            TreinSelector.setSelected(true);
            RouteManager.setTransportationTrain();
        }
        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 1);
        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 2);
        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 3);
    }

    // ROUTEBESCHRIJVING
    public void getRoutesAndTimes(ActionEvent event) {
        String vertrek = VertrekChoiceBox.getValue() != null ? VertrekChoiceBox.getValue() : "Geen halte geselecteerd";
        String aankomst = AankomstChoiceBox.getValue() != null ? AankomstChoiceBox.getValue() : "Geen halte geselecteerd";
        LocalDate selectedDate = datePicker.getValue()!= null ? datePicker.getValue() : LocalDate.parse("Geen datum geselecteerd");
        String selectedTime = timeComboBox.getValue() != null ? timeComboBox.getValue() : "Geen tijd geselecteerd";

        // Calculate route time
        String routeTime = RouteManager.getRouteTime(calculateDepartureTime(1), aankomst);

        String currentDate = getCurrentDate(selectedDate);

        // Use routeTime for departure and arrival times
        String arrivalTime = calculateDestinationTime(selectedTime, Integer.parseInt(RouteManager.getRouteTime(vertrek,aankomst)));

        // Update traject buttons based on the selected stops and the current page
        updateTrajectButtons(vertrek, aankomst, 1);
        updateTrajectButtons(vertrek, aankomst, 2);
        updateTrajectButtons(vertrek, aankomst, 3);

        String result = "Vertrek: " + vertrek + "\nAankomst: " + aankomst + "\nDatum: " + currentDate + "\nTijd: " + selectedTime
                + "\nVertrektijd: " + selectedTime + "\nAankomsttijd: " + arrivalTime
                + "\nReistijd: " + routeTime;

        // Display selected time and destination time in the selectedItemTextArea
        selectedItemTextArea.setText(result);
    }

    public String getRouteTime(String vertrek, String aankomst) {
        if (routeTimes.containsKey(vertrek) && routeTimes.get(vertrek).containsKey(aankomst)) {
            String distanceTime = routeTimes.get(vertrek).get(aankomst);
            int timeInMinutes = convertTimeToMinutes(distanceTime);
            return String.valueOf(timeInMinutes);
        } else {
            return "Zelfde halte geselecteerd";
        }
    }

    private int convertTimeToMinutes(String time) {
        String[] parts = time.split("[^\\d]+");

        int hours = 0;
        int minutes = 0;

        if (parts.length > 0) {
            hours = Integer.parseInt(parts[0]);
        }

        if (time.contains("u") && parts.length > 1) {
            minutes = Integer.parseInt(parts[1]);
        }

        return hours + minutes;
    }

    private String getCurrentDate(LocalDate selectedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return selectedDate != null ? selectedDate.format(formatter) : "Geen datum geselecteerd";
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(new Date());
    }

    private void updateTextArea(String myVertrek, String myAankomst) {
        String result = "Vertrek: " + myVertrek + "\nAankomst: " + myAankomst;

        // Update buttons with selected stops
        updateTrajectButtons(myVertrek, myAankomst,1);
        updateTrajectButtons(myVertrek, myAankomst,2);
        updateTrajectButtons(myVertrek, myAankomst,3);

    }

    // EERDERE TIJDEN WEERGEVEN
    @FXML
    private void handleBtnVroeger(ActionEvent event) {
        currentPage--;
        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 1);
        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 2);
        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 3);
    }

    // LATERE TIJDEN WEERGEVEN
    @FXML
    private void handleBtnLater(ActionEvent event) {
        currentPage++;
        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 1);
        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 2);
        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), 3);
    }

    // AANKOMSTTIJDEN BEREKENEN
    private String calculateDestinationTime(String selectedTime, int routeTime) {
        // Parse the selected time to extract hours and minutes
        String[] timeParts = selectedTime.split(":");
        int selectedHours = Integer.parseInt(timeParts[0]);
        int selectedMinutes = Integer.parseInt(timeParts[1]);

        // Calculate destination time by adding route time to selected time
        int destinationHours = (selectedHours + (selectedMinutes + routeTime) / 60) % 24;
        int destinationMinutes = (selectedMinutes + routeTime) % 60;

        // Format the destination time
        return String.format("%02d:%02d", destinationHours, destinationMinutes);
    }

    // VERTREKTIJDEN BEREKENEN
    public class DepartureTimeCalc {

        public DepartureTimeCalc(int trajectoryIndex) {
        }

        public static String calculateDepartureTime(int trajectoryIndex) {
            DecimalFormat df = new DecimalFormat("00");

            // 5:04
            int startTime = 5 * 60 + 4;

            // 23:35
            int endTime = 23 * 60 + 35;

            // Interval in minutes
            int interval = 25;
            final int Traject_PER_TABLE = 3;
            int startTraject = (trajectoryIndex - 1) + 1;
            int endTraject = startTraject + Traject_PER_TABLE - 1;

            // Set var current time to display selected time

            int currentTime;
            // Calculate the specific timestamp for the given trajectory index
            currentTime = startTime + ((startTraject - 1) + ((currentPage - 1) * 3)) * interval;
            int hours = currentTime / 60;
            int minutes = currentTime % 60;

            return df.format(hours) + ":" + df.format(minutes);
        }
    }

    // TRAJECTEN
    @FXML
    private void handleTrajectButton(ActionEvent event) {

        String buttonText = null;
        Button clickedButton = (Button) event.getSource();
        buttonText = clickedButton.getText();
        int trajectoryIndex;

        // Determine the trajectory index based on the clicked button
        if (buttonText.contains("1")) {
            trajectoryIndex = 1;
        } else if (buttonText.contains("2")) {
            trajectoryIndex = 2;
        } else if (buttonText.contains("3")) {
            trajectoryIndex = 3;
        } else {
            // Default to Traject_1 if the button text doesn't contain 1, 2, or 3
            trajectoryIndex = 1;
        }

        updateTrajectButtons(VertrekChoiceBox.getValue(), AankomstChoiceBox.getValue(), trajectoryIndex);
    }
    private void updateTextAreaFromButton(String buttonText,int trajectoryIndex) {
        String vertrek = VertrekChoiceBox.getValue() != null ? VertrekChoiceBox.getValue() : "Geen halte geselecteerd";
        String aankomst = AankomstChoiceBox.getValue() != null ? AankomstChoiceBox.getValue() : "Geen halte geselecteerd";
        LocalDate selectedDate = datePicker.getValue()!= null ? datePicker.getValue() : LocalDate.parse("Geen datum geselecteerd");
        String selectedTime = timeComboBox.getValue() != null ? timeComboBox.getValue() : "Geen tijd geselecteerd";

        // Set destination time
        String departureTime = String.valueOf(new DepartureTimeCalc(trajectoryIndex));
        // Calculate route time
        int routeTime = Integer.parseInt((RouteManager.getRouteTime(vertrek, aankomst)));
        String currentDate = getCurrentDate(selectedDate);
        // Use routeTime for departure and arrival times
        String arrivalTime = calculateDestinationTime(selectedTime, routeTime);

        String result = "Vertrek: " + vertrek  + "\nAankomst : " + aankomst +"\n\n\nTijd: " + departureTime + " --> "
                + arrivalTime + "\n"  + "\n\nDatum: " + currentDate +  "\nReistijd: "+ routeTime + " minuten";

        // Update buttons with selected stops
        updateTrajectButtons(vertrek, aankomst,1);
        updateTrajectButtons(vertrek, aankomst,2);
        updateTrajectButtons(vertrek, aankomst,3);

        // Display selected time and destination time in the selectedItemTextArea
        selectedItemTextArea.setText(result);
    }

    public void updateTrajectButtons(String vertrek, String aankomst, int trajectoryIndex) {
        if (vertrek != null && aankomst != null && timeComboBox != null && datePicker != null) {
            String selectedTime = timeComboBox.getValue();

            try {
                // Get route time in minutes
                int routeTime = Integer.parseInt(getRouteTime(vertrek, aankomst));

                // Calculate destination time
                String destinationTime1 = calculateDestinationTime(calculateDepartureTime(1), routeTime);
                String destinationTime2 = calculateDestinationTime(calculateDepartureTime(2), routeTime);
                String destinationTime3 = calculateDestinationTime(calculateDepartureTime(3), routeTime);

                // Calculate departure time for the specified trajectory index
                String departureTime = String.valueOf(new DepartureTimeCalc(trajectoryIndex));

                Traject_1.setText(vertrek + "\nVertrektijd: "+ calculateDepartureTime(1) + "\n" + aankomst +
                        "\n" +"Aankomsttijd: " + destinationTime1 + "\nReistijd: " + RouteManager.getRouteTime(vertrek,aankomst));
                Traject_2.setText(vertrek + "\nVertrektijd: "+ calculateDepartureTime(2) + "\n" + aankomst +
                        "\n" +"Aankomsttijd: " + destinationTime2 + "\nReistijd: " + RouteManager.getRouteTime(vertrek, aankomst));
                Traject_3.setText(vertrek + "\nVertrektijd: "+ calculateDepartureTime(3) + "\n" + aankomst +
                        "\n" +"Aankomsttijd: " + destinationTime3 + "\nReistijd: " + RouteManager.getRouteTime(vertrek, aankomst));
            } catch (NumberFormatException e) {
                // Handle the case when parsing fails (invalid number format)
                System.err.println("Error: Ongeldige waarde voor routeTime");
                // You can display an error message or take appropriate action
            }
        } else {
            // Handle the case when either vertrek or aankomst is null
            // You can display an error message or take appropriate action
            System.out.println("Error: Vertrek of aankomst is leeg");
        }
    }

    public void updateTrajectButtons(String vertrek, String aankomst) {
        if (vertrek != null && aankomst != null) {
            String reistijd = getRouteTime(vertrek, aankomst) + " " + ResourceBundleManager.getString("minutes");

            Traject_1.setText(ResourceBundleManager.getString("traject.button.1")
                    .replace("{vertrek}", vertrek)
                    .replace("{aankomst}", aankomst)
                    .replace("{reistijd}", reistijd));

            Traject_2.setText(ResourceBundleManager.getString("traject.button.2")
                    .replace("{vertrek}", vertrek)
                    .replace("{aankomst}", aankomst)
                    .replace("{reistijd}", reistijd));

            Traject_3.setText(ResourceBundleManager.getString("traject.button.3")
                    .replace("{vertrek}", vertrek)
                    .replace("{aankomst}", aankomst)
                    .replace("{reistijd}", reistijd));
        } else {
            // Behandel het geval waarin vertrek of aankomst null is
            // Je kunt een foutmelding weergeven of een passende actie ondernemen
            System.out.println(ResourceBundleManager.getString("error.departure.arrival.null"));
        }
    }

    // FAVORIETE HALTES
    // favoriete haltes toevoegen
    @FXML
    private void addToFavorites(ActionEvent event) {
        String vertrek = VertrekChoiceBox.getValue();
        String aankomst = AankomstChoiceBox.getValue();

        if (vertrek != null && aankomst != null) {
            if (!isRouteAlreadyAdded(vertrek, aankomst)) {
                FavoriteRoute favoriteRoute = new FavoriteRoute();
                favoriteRoute.setVertrek(vertrek);
                favoriteRoute.setAankomst(aankomst);
                favoriteRoutes.add(favoriteRoute);
                favoritesListView.setItems(FXCollections.observableArrayList(favoriteRoutes));
            } else {
                System.out.println("Error: Route is al toegevoegd aan favorieten");
                // Toon een melding aan de gebruiker dat de route al is toegevoegd
            }
        } else {
            System.out.println("Error: Vertrek of Aankomst is leeg");
        }
    }

    // favoriete haltes in vertrek- en aankomstveld weergeven
    @FXML
    private void handleFavoriteRouteSelection(MouseEvent event) {
        FavoriteRoute selectedFavoriteRoute = favoritesListView.getSelectionModel().getSelectedItem();
        if (selectedFavoriteRoute != null) {
            setSelectedFavoriteRoute(selectedFavoriteRoute);
        }
    }

    public void setSelectedFavoriteRoute(FavoriteRoute favoriteRoute) {
        VertrekChoiceBox.setValue(favoriteRoute.getVertrek());
        AankomstChoiceBox.setValue(favoriteRoute.getAankomst());
    }

    // favoriete halte maximaal 1 keer toevoegen
    private boolean isRouteAlreadyAdded(String vertrek, String aankomst) {
        for (FavoriteRoute route : favoriteRoutes) {
            if (route.getVertrek().equals(vertrek) && route.getAankomst().equals(aankomst)) {
                return true;  // Route is al toegevoegd
            }
        }
        return false;  // Route is nog niet toegevoegd
    }

    // favoriete halte verwijderen
    private void removeFavoriteRoute(String vertrek, String aankomst) {
        FavoriteRoute routeToRemove = null;

        for (FavoriteRoute route : favoriteRoutes) {
            if (route.getVertrek().equals(vertrek) && route.getAankomst().equals(aankomst)) {
                routeToRemove = route;
                break;
            }
        }

        if (routeToRemove != null) {
            favoriteRoutes.remove(routeToRemove);
            favoritesListView.setItems(FXCollections.observableArrayList(favoriteRoutes));
        }
    }

    @FXML
    private void removeFromFavorites(ActionEvent event) {
        FavoriteRoute selectedRoute = favoritesListView.getSelectionModel().getSelectedItem();

        if (selectedRoute != null) {
            removeFavoriteRoute(selectedRoute.getVertrek(), selectedRoute.getAankomst());
        } else {
            // Toon een melding aan de gebruiker dat er geen route is geselecteerd
            System.out.println("Error: Geen favoriete route geselecteerd om te verwijderen");
        }
    }

    // INLOGGEN
    public void showLoginScreen(ActionEvent event) {
        try {
            if (mainContainer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                Parent loginScreen = loader.load();

                // Get the controller associated with the login-view.fxml
                LoginController loginController = loader.getController();

                // Set the reference to the main container and hello controller in the login controller
                loginController.setMainContainer(mainContainer);
                loginController.setMainController(this);

                mainContainer.getChildren().setAll(loginScreen);
            } else {
                System.out.println("Error: mainContainer is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml")); // Replace with your main screen FXML file
            VBox mainScreenPane = loader.load();

            // Set up the controller for the main screen if needed

            mainContainer.getChildren().setAll(mainScreenPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setMainContainer(VBox mainContainer) {
        this.mainContainer = mainContainer;
    }

    // VERTALEN
    public void changeTaalButton() {
        if (TaalButton.getText().equals("EN")) {
            TaalButton.setText("NL");
            ResourceBundleManager.setLocale(new Locale("en"));
        } else {
            TaalButton.setText("EN");
            ResourceBundleManager.setLocale(new Locale("nl"));
        }
        VertrekLabel.setText(ResourceBundleManager.getString("departure"));
        AankomstLabel.setText(ResourceBundleManager.getString("arrival"));
        datePicker.setPromptText(ResourceBundleManager.getString("select_date"));
        timeComboBox.setPromptText(ResourceBundleManager.getString("select_time"));
        refreshPage.setText(ResourceBundleManager.getString("refresh_page"));
        wisselHaltes.setText(ResourceBundleManager.getString("Swap"));
        WijzigLettergrootte.setText(ResourceBundleManager.getString("change_fontsize"));
        smallFontSize.setText(ResourceBundleManager.getString("small"));
        middleFontSize.setText(ResourceBundleManager.getString("middle"));
        bigFontSize.setText(ResourceBundleManager.getString("big"));
    }
}