/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.appinvestigacion;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author luis
 */
public class ProductoController implements Initializable {

    private static final Logger LOG = Logger.getLogger(PrimaryController.class.getName());

    @FXML
    private TableColumn<Producto, Integer> colCodigo;
    @FXML
    private TableColumn<Producto, String> colDescripcion;
    @FXML
    private TableColumn<Producto, Integer> colCantidad;
    @FXML
    private TableColumn<Producto, Integer> colIva;
    @FXML
    private TableColumn<Producto, Float> colPrecio;
    @FXML
    private TableColumn<Producto, Integer> colMarcas;

    Connection con = null;

    @FXML
    private TextField txfId;
    @FXML
    private TextField txfDescripcion;
    @FXML
    private TextField txfCantidad;
    @FXML
    private TextField txfPrecio;
    @FXML
    private TableView<Producto> tblDatos;
    private TextField txfMarca;
    @FXML
    private ComboBox<Marca> cmbMarca;
    @FXML
    private ComboBox<String> cmbIva;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        this.colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        this.colIva.setCellValueFactory(new PropertyValueFactory<>("iva"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        this.colMarcas.setCellValueFactory(new PropertyValueFactory<>("marca"));

        this.cmbMarca.setCellFactory((ListView<Marca> l) -> {
            return new ListCell<Marca>() {
                @Override
                protected void updateItem(Marca m, boolean empty) {
                    if (!empty) {
                        this.setText("(" + m.getCodigo() + ") " + m.getDescripcion());
                    } else {
                        this.setText("");
                    }
                    super.updateItem(m, empty);
                }
            };
        });
        this.cmbMarca.setButtonCell(new ListCell<Marca>() {
            @Override
            protected void updateItem(Marca m, boolean empty) {
                if (!empty) {
                    this.setText("(" + m.getCodigo() + ") " + m.getDescripcion());
                } else {
                    this.setText("");
                }
                super.updateItem(m, empty);
            }
        }
        );

        this.cmbIva.getItems().add("10%");
        this.cmbIva.getItems().add("5%");
        this.cmbIva.getItems().add("Excento");
        this.cmbIva.getSelectionModel().selectFirst();

        try {
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/proyecto_investigacion", "admin", "123456");
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
            Alert al = new Alert(AlertType.INFORMATION);
            al.setTitle("Error de conexion");
            al.setHeaderText("No se puede conectar al base de datos");
            al.setContentText(ex.toString());
            al.showAndWait();
            System.out.println("OK");
            System.exit(1);
        }
        this.cargarDatos();
        this.cargarMarca();

        this.tblDatos.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Producto> obs, Producto valorAterior, Producto valorNuevo) -> {
            if (valorNuevo != null) {
                this.txfId.setText(valorNuevo.getCodigo().toString());
                this.txfDescripcion.setText(valorNuevo.getDescripcion());
                this.txfCantidad.setText(valorNuevo.getCantidad().toString());
                this.txfPrecio.setText(valorNuevo.getPrecio().toString());
                for (Marca mc : this.cmbMarca.getItems()) {
                    if (mc.getCodigo().equals(this.tblDatos.getSelectionModel().getSelectedItem().getMarca())) {
                        this.cmbMarca.getSelectionModel().select(mc);
                        break;
                    }
                }
            }
        });

    }

    private void cargarMarca() {
        try {
            String sql = "SELECT * FROM marca";
            Statement stm = this.con.createStatement();
            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                this.cmbMarca.getItems().add(new Marca(resultado.getInt("idmarca"), resultado.getString("descripcion")));
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar datos de la BD", ex);
        }
    }

    private void cargarDatos() {
        this.tblDatos.getItems().clear();
        try {
            String sql = "SELECT * FROM producto";
            Statement stm = this.con.createStatement();
            ResultSet resultado = stm.executeQuery(sql);
            while (resultado.next()) {
                Integer cod = resultado.getInt("idproducto");
                String desc = resultado.getString("descripcion");
                Integer cant = resultado.getInt("cantidad");
                Integer iv = resultado.getInt("iva");
                Float prec = resultado.getFloat("precio");
                Integer marc = resultado.getInt("idmarca");
                Producto p = new Producto(cod, desc, cant, iv, prec, marc);
                this.tblDatos.getItems().add(p);
            }
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al cargar datos de la BD", ex);
        }
    }

    @FXML
    private void onRegistrar(ActionEvent event) {
        String id = this.txfId.getText();
        String descripcion = this.txfDescripcion.getText();
        String cantidad = this.txfCantidad.getText();
        String iva = this.cmbIva.getSelectionModel().getSelectedItem();
        Integer iv;
        switch (iva) {
            case "10%":
                iv = 10;
                break;
            case "5%":
                iv = 5;
                break;
            default:
                iv = 0;
                break;
        }
        String precio = this.txfPrecio.getText();
        Integer marca = this.cmbMarca.getSelectionModel().getSelectedItem().getCodigo();

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_investigacion", "admin", "12345");
            String sql = "INSERT INTO producto(descripcion, cantidad, iva, precio, idmarca) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, descripcion);
            stm.setString(2, cantidad);
            stm.setInt(3, iv);
            stm.setString(4, precio);
            stm.setInt(5, marca);
            stm.execute();
            Alert al = new Alert(AlertType.INFORMATION);
            al.setTitle("Exito");
            al.setHeaderText("Producto guardada correctamente");
            al.showAndWait();
            this.txfDescripcion.clear();
            this.txfId.clear();
            this.cargarMarca();
            this.cargarDatos();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
        }
    }

    @FXML
    private void onEditar(ActionEvent event) {
        String id = this.txfId.getText();
        String descripcion = this.txfDescripcion.getText();
        String cantidad = this.txfCantidad.getText();
        String iva = this.cmbIva.getSelectionModel().getSelectedItem();
        Integer iv;
        switch (iva) {
            case "10%":
                iv = 10;
                break;
            case "5%":
                iv = 5;
                break;
            default:
                iv = 0;
                break;
        }
        String precio = this.txfPrecio.getText();
        Integer marca = this.cmbMarca.getSelectionModel().getSelectedItem().getCodigo();
        String sql = "UPDATE producto SET descripcion=?, cantidad=?, iva=?, precio=?, idmarca=? WHERE idproducto = ?";
        try {

            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, descripcion);
            stm.setString(2, cantidad);
            stm.setInt(3, iv);
            stm.setString(4, precio);
            stm.setInt(5, marca);
            stm.setInt(6, Integer.parseInt(id));
            stm.execute();
            Alert al = new Alert(AlertType.INFORMATION);
            al.setTitle("Exito");
            al.setHeaderText("Marca editada correctamente");
            al.showAndWait();
            this.txfDescripcion.clear();
            this.txfId.clear();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, "Error al editar", ex);
            Alert al = new Alert(AlertType.INFORMATION);
            al.setTitle("Error de conexion");
            al.setHeaderText("No se puede editar registro en la base de datos");
            al.showAndWait();
        }
        this.cargarDatos();
        this.cargarMarca();
    }

    @FXML
    private void onLimpiar(ActionEvent event) {
        this.txfDescripcion.setText("");
        this.txfId.setText("");
        this.txfCantidad.setText("");
        this.txfPrecio.setText("");
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        String strCodigo = this.txfId.getText();
        String strDescripcion = this.txfDescripcion.getText();

        if (strCodigo.isEmpty()) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Error al eliminar");
            a.setHeaderText("Ingrese un codigo");
            a.show();
        } else {
            Alert alConfirm = new Alert(AlertType.CONFIRMATION);
            alConfirm.setTitle("Confirmar");
            alConfirm.setHeaderText("Desea eliminar el producto?");
            alConfirm.setContentText(strCodigo + " - " + strDescripcion);
            Optional<ButtonType> accion = alConfirm.showAndWait();
            if (accion.get().equals(ButtonType.OK)) {
                try {
                    String sql = "DELETE FROM producto WHERE idproducto = ?";
                    PreparedStatement stm = con.prepareStatement(sql);
                    Integer cod = Integer.parseInt(strCodigo);
                    stm.setInt(1, cod);
                    stm.execute();
                    int cantidad = stm.getUpdateCount();
                    if (cantidad == 0) {
                        Alert a = new Alert(AlertType.ERROR);
                        a.setTitle("Error al eliminar");
                        a.setHeaderText("No existe el producto con codigo " + strCodigo);
                        a.show();
                    } else {
                        Alert a = new Alert(AlertType.INFORMATION);
                        a.setTitle("Eliminado");
                        a.setHeaderText("Producto eliminado correctamente.");
                        a.show();
                        this.cargarDatos();
                    }
                } catch (SQLException ex) {
                    LOG.log(Level.SEVERE, "Error al elminar", ex);
                }
            }
        }
    }

}
