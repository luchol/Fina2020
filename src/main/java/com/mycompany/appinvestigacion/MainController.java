/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.appinvestigacion;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author luis
 */
public class MainController implements Initializable {
    private static final Logger LOG = Logger.getLogger(MainController.class.getName());

    @FXML
    private TabPane tapPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onMarca(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader();
            AnchorPane vistaMarca = loader.load(MainController.class.getResourceAsStream("primary.fxml"));
            Tab t = new Tab();
            t.setText("Marcas");
            t.setContent(vistaMarca);
            this.tapPane.getTabs().add(t);
            this.tapPane.getSelectionModel().select(t);
        }catch(IOException ex){
            LOG.log(Level.SEVERE, "Error al cargar FXML en marcas", ex);
        }
    }

    @FXML
    private void onProducto(ActionEvent event) {
         try{
            FXMLLoader loader = new FXMLLoader();
            AnchorPane vistaProducto = loader.load(MainController.class.getResourceAsStream("producto.fxml"));
            Tab t = new Tab();
            t.setText("Productos");
            t.setContent(vistaProducto);
            this.tapPane.getTabs().add(t);
            this.tapPane.getSelectionModel().select(t);
        }catch(IOException ex){
            LOG.log(Level.SEVERE, "Error al cargar FXML en productos", ex);
        }
    }
    
    
}
