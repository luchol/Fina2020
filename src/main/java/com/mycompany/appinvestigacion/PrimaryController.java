package com.mycompany.appinvestigacion;

import java.io.IOException;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController implements Initializable{
    
    private static final Logger LOG = Logger.getLogger(PrimaryController.class.getName());

    @FXML
    private TextField txfId;
    @FXML
    private TextField txfDescripcion;
    
    Connection con = null;
    @FXML
    private TableView<Marca> tblDatos;
    @FXML
    private TableColumn<Marca, Integer> colCodigo;
    @FXML
    private TableColumn<Marca, String> colDescripcion;
    @FXML
    private Button btnEliminar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        
        try{
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_investigacion", "admin", "12345");
        }catch(SQLException ex){
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
            Alert al=new Alert(AlertType.INFORMATION);
            al.setTitle("Error de conexion");
            al.setHeaderText("No se puede conectar al base de datos");
            al.setContentText(ex.toString());
            al.showAndWait();
            System.exit(1);
        }
        this.cargarDatos();
        
        this.tblDatos.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Marca> obs, Marca valorAterior, Marca valorNuevo) -> {
            if(valorNuevo != null){
            this.txfId.setText(valorNuevo.getCodigo().toString());
            this.txfDescripcion.setText(valorNuevo.getDescripcion());
            }
        });
    }
    
    private void cargarDatos(){
        this.tblDatos.getItems().clear();
        try{
            String sql = "SELECT * FROM marca";
            Statement stm = this.con.createStatement();
            ResultSet resultado = stm.executeQuery(sql);
            while(resultado.next()){
                Integer cod = resultado.getInt("idmarca");
                String desc = resultado.getString("descripcion");
                Marca m = new Marca(cod, desc);
                this.tblDatos.getItems().add(m);
            }
        }catch(SQLException ex){
            LOG.log(Level.SEVERE, "Error al cargar datos de la BD", ex);
        }
    }

    @FXML
    private void onMostrar(ActionEvent event) {
        String id = this.txfId.getText();
        String descripcion = this.txfDescripcion.getText();
        
        
        try{
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_investigacion", "admin", "12345");
            String sql="INSERT INTO marca(descripcion) VALUES(?)";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, descripcion);
            stm.execute();
            Alert al=new Alert(AlertType.INFORMATION);
            al.setTitle("Exito");
            al.setHeaderText("Marca guardada correctamente");
            al.showAndWait();
            this.txfDescripcion.clear();
            this.txfId.clear();
            this.cargarDatos();
        }catch(SQLException ex){
            LOG.log(Level.SEVERE, "Error al conectar a la base de datos", ex);
        }
    }

    @FXML
    private void onEditar(ActionEvent event) {
        String id = this.txfId.getText();
        String descripcion = this.txfDescripcion.getText();
        String sql="UPDATE marca SET descripcion = ? WHERE idmarca = ?";
        
        try{
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, descripcion);
            stm.setInt(2, Integer.parseInt(id));
            stm.execute();
            Alert al=new Alert(AlertType.INFORMATION);
            al.setTitle("Exito");
            al.setHeaderText("Marca editada correctamente");
            al.showAndWait();
            this.txfDescripcion.clear();
            this.txfId.clear();
        }catch(SQLException ex){
            LOG.log(Level.SEVERE, "Error al editar", ex);
            Alert al=new Alert(AlertType.INFORMATION);
            al.setTitle("Error de conexion");
            al.setHeaderText("No se puede editar registro en la base de datos");
            al.showAndWait();
        }
        this.cargarDatos();
    }

    @FXML
    private void onLimpiar(ActionEvent event) {
        this.txfDescripcion.setText("");
        this.txfId.setText("");
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        String strCodigo = this.txfId.getText();
        String strDescripcion = this.txfDescripcion.getText();
        
        if (strCodigo.isEmpty()){
            Alert a= new Alert(AlertType.ERROR);
            a.setTitle("Error al eliminar");
            a.setHeaderText("Ingrese un codigo");
            a.show();
        } else {
            Alert alConfirm=new Alert(AlertType.CONFIRMATION);
            alConfirm.setTitle("Confirmar");
            alConfirm.setHeaderText("Desea eliminar la marca?");
            alConfirm.setContentText(strCodigo +" - "+strDescripcion);
            Optional<ButtonType> accion = alConfirm.showAndWait();
            if(accion.get().equals(ButtonType.OK)){
            try{
                String sql = "DELETE FROM marca WHERE idmarca = ?";
                PreparedStatement stm = con.prepareStatement(sql);
                Integer cod=Integer.parseInt(strCodigo);
                stm.setInt(1, cod);
                stm.execute();
                int cantidad = stm.getUpdateCount();
                if(cantidad == 0){
                    Alert a= new Alert(AlertType.ERROR);
                    a.setTitle("Error al eliminar");
                    a.setHeaderText("No existe la marca con codigo "+strCodigo);
                    a.show(); 
                }else{
                    Alert a= new Alert(AlertType.INFORMATION);
                    a.setTitle("Eliminado");
                    a.setHeaderText("Marca eliminada correctamente.");
                    a.show();
                    this.cargarDatos();
                }
            } catch(SQLException ex){
                LOG.log(Level.SEVERE, "Error al elminar", ex);
            }
          }
        }
    }  
}
