package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Product;
import services.ProductService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Stat implements Initializable {
    private final ProductService ps = new ProductService();

    @FXML
    private PieChart associationDonationChart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updatePieChartData();



    }

    private void updatePieChartData() {
        try {
            Map<String, Integer> productsStatistics = ps.getProductsStatistics();

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            for (Map.Entry<String, Integer> entry : productsStatistics.entrySet()) {
                String productName = entry.getKey();
                int totalQuantity = entry.getValue();
                String dataLabel = productName + " : " + totalQuantity + "DT";
                pieChartData.add(new PieChart.Data(dataLabel, totalQuantity));
            }

            associationDonationChart.setData(pieChartData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void back(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Asma/afficherProduit_client.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) event.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}