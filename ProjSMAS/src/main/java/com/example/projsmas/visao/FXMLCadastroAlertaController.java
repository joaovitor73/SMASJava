package com.example.projsmas.visao;

import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.ResourceBundle;

import com.example.projsmas.Main;
import com.example.projsmas.aplicacao.Alerta;
import com.example.projsmas.aplicacao.MunicipioEspecie;
import com.example.projsmas.persistencia.AlertaDao;
import com.example.projsmas.persistencia.EspecieDao;
import com.example.projsmas.persistencia.MunicipioDao;
import com.example.projsmas.persistencia.MunicipioEspecieDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import com.example.projsmas.Main;
import com.example.projsmas.aplicacao.Usuario;
import com.example.projsmas.persistencia.UsuarioDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;


public class FXMLCadastroAlertaController extends LoginController implements Initializable {
	private EspecieDao especieDao = new EspecieDao();
	private AlertaDao alertaDao = new AlertaDao();
	private MunicipioEspecieDao municipioEspecieDao = new MunicipioEspecieDao();
	private Alerta alerta;
	private MunicipioDao municipioDao = new MunicipioDao();
	@FXML
	private AnchorPane apnCadastroAlerta, EditarAlerta, CriarAlerta;
	@FXML
	private Label warnings, ouLabel;
	@FXML
	private Button btnCadastrar1, btnExcluir;
	@FXML
	private TextField txtMunicipio, txtEspecie, txtNome, txtData, qualquerID;
	@FXML
	private TextArea txtAlerta, txtAlerta1;
	@FXML
	private ComboBox<Integer> comboBoxAlertas;
	@FXML
	private ComboBox<String> comboEspecie;
	@FXML
	private ComboBox<String> comboEspecieCadastrar;
	@FXML
	private ComboBox<String> comboCidades;
	@FXML
	private ComboBox<String> comboCidadesCadastrar;
	@FXML
	private Stage stage;
	private ObservableList<Integer> idAlertas = FXCollections.observableArrayList();
	private ObservableList<String> relatorioEspecie = FXCollections.observableArrayList();
	private ObservableList<String> listaMunicipios = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		CriarAlerta.setVisible(true);
		EditarAlerta.setVisible(false);
		btnExcluir.setDisable(true);
		comboEspecie.setDisable(true);
		comboCidades.setDisable(true);
		txtAlerta1.setDisable(true);
		apnCadastroAlerta.setVisible(true);
		idAlertas.addAll(alertaDao.selectEmail(getUser().getEmail()));
		FXCollections.sort(idAlertas);
		comboBoxAlertas.setItems(idAlertas);
		relatorioEspecie.addAll(especieDao.relatorioNomes());
		FXCollections.sort(relatorioEspecie);
		comboEspecie.setItems(relatorioEspecie);
		comboEspecieCadastrar.setItems(relatorioEspecie);
		listaMunicipios.addAll(municipioDao.relatorioNomes());
		FXCollections.sort(listaMunicipios);
		comboCidades.setItems(listaMunicipios);
		comboCidadesCadastrar.setItems(listaMunicipios);
	}
	@FXML
	private void handleBtnCadastrarAction() {
		warnings.setVisible(false);
		if(!(txtAlerta.getText().equals("")) && ((comboEspecieCadastrar.getValue() != null) && (comboCidadesCadastrar.getValue()!= null)) && (!(comboEspecieCadastrar.getValue().equals("Especie")) && !(comboCidadesCadastrar.getValue().equals("Municipio")))){
			try {
				Alerta alerta = new Alerta();
				alerta.setDescricao(txtAlerta.getText());
				alerta.setEmailUsuario(getUser().getEmail());
				alerta.setIdEspecie(especieDao.selectName(comboEspecieCadastrar.getValue()).getId());
				alertaDao.insert(alerta);
				ArrayList<Alerta> lista = alertaDao.selectAll();
				alerta = lista.get(lista.size() - 1);
				MunicipioEspecie m = new MunicipioEspecie(municipioDao.selectNameAndUf(comboCidadesCadastrar.getValue(), "RN").getId(), alerta.especiId(), alerta.getId());
				municipioEspecieDao.insert(m);
				txtAlerta.setText("");
				warnings.setVisible(true);
				warnings.setTextFill(Paint.valueOf("#00f731"));
				warnings.setText("Alerta cadastrado!");
				comboBoxAlertas.getItems().clear();
				idAlertas.addAll(alertaDao.selectEmail(getUser().getEmail()));
				FXCollections.sort(idAlertas);
				comboBoxAlertas.setItems(idAlertas);

			}catch (Exception e){
				System.out.println(e);
			}
		}else{
			warnings.setVisible(true);
			warnings.setTextFill(Paint.valueOf("#ff0000"));
			warnings.setText("Preencha todos os campos!");
		}
	}
	@FXML
	private void EditarAlertas() {
		CriarAlerta.setVisible(false);
		EditarAlerta.setVisible(true);
		btnCadastrar1.setDisable(true);
		btnExcluir.setDisable(true);
		comboEspecie.setDisable(true);
		comboCidades.setDisable(true);
		txtAlerta1.setDisable(true);
		txtAlerta1.setText("");
		if(getUser().getFuncao() != 3) {
			qualquerID.setVisible(false);
			ouLabel.setVisible(false);
		}else{
			qualquerID.setVisible(true);
			ouLabel.setVisible(true);
		}
		txtAlerta.setText("");
		if(!comboCidades.getItems().contains("Municipio")) {
			comboCidades.getItems().add("Municipio");
		}
		comboCidades.setValue("Municipio");

		if(!comboEspecie.getItems().contains("Especie")) {
			comboEspecie.getItems().add("Especie");
		}
		comboEspecie.setValue("Especie");
	}
	@FXML
	private void voltarEditarAlerta() {
		CriarAlerta.setVisible(true);
		EditarAlerta.setVisible(false);
		if(!comboCidadesCadastrar.getItems().contains("Municipio")) {
			comboCidadesCadastrar.getItems().add("Municipio");
		}
		comboCidadesCadastrar.setValue("Municipio");

		if(!comboEspecieCadastrar.getItems().contains("Especie")) {
			comboEspecieCadastrar.getItems().add("Especie");
		}
		comboEspecieCadastrar.setValue("Especie");
	}
	@FXML
	private void pesquisasrAlertaID() {
		warnings.setVisible(false);
		btnCadastrar1.setDisable(true);
		if(comboBoxAlertas.getValue()==null){
			warnings.setVisible(true);
			warnings.setTextFill(Paint.valueOf("#ff0000"));
			warnings.setText("Selecione um id o campo ID!");
		}else{
			alerta = alertaDao.selectId(comboBoxAlertas.getValue());
			if(alerta != null){
			 	btnCadastrar1.setDisable(false);
				btnExcluir.setDisable(false);
				comboEspecie.setDisable(false);
				comboCidades.setDisable(false);
				txtAlerta1.setDisable(false);
				comboEspecie.setValue(especieDao.selectId(alerta.especiId()).getNome());
				txtAlerta1.setText(alerta.getDescricao());
			}else{
				warnings.setVisible(true);
				warnings.setTextFill(Paint.valueOf("#ff0000"));
				warnings.setText("Alerta não encontrado!");
			}
		}
	}
	@FXML
	protected void ExcluirAlerta(){
		municipioEspecieDao.deleteIdAlerta(alerta.getId());
		alertaDao.delete(alerta.getId());
		btnCadastrar1.setDisable(true);
		btnExcluir.setDisable(true);
		comboEspecie.setDisable(true);
		comboCidades.setDisable(true);
		txtAlerta1.setDisable(true);
		txtAlerta1.setText("");
		comboBoxAlertas.getItems().clear();
		idAlertas.addAll(alertaDao.selectEmail(getUser().getEmail()));
		FXCollections.sort(idAlertas);
		comboBoxAlertas.setItems(idAlertas);
		comboEspecie.setPromptText("Especie");
		comboCidades.setPromptText("Municipio");
		warnings.setVisible(true);
		warnings.setTextFill(Paint.valueOf("#00f731"));
		warnings.setText("Alerta Exluido!");
		btnExcluir.setDisable(true);
		comboEspecie.setDisable(true);
		comboCidades.setDisable(true);
		txtAlerta1.setDisable(true);
	}
	@FXML
	private void handleBtnSalvarAction() {
		boolean flag = false;
		if(!(txtAlerta1.getText().equals(alerta.getDescricao())) && !(txtAlerta1.getText().equals(""))){
			alerta.setDescricao(txtAlerta1.getText());
			flag = true;
		};
		if(!(comboEspecie.getValue().equals(especieDao.selectId(alerta.especiId()).getNome())) && !(comboEspecie.getValue().equals(""))){
			alerta.setIdEspecie(especieDao.selectName(comboEspecie.getValue()).getId());
			flag = true;
		};
		if(flag){
			alertaDao.update(alerta.getId(), alerta);
			warnings.setVisible(true);
			warnings.setTextFill(Paint.valueOf("#00f731"));
			warnings.setText("Alerta Editado!");
			btnExcluir.setDisable(true);
			comboEspecie.setDisable(true);
			comboCidades.setDisable(true);
			txtAlerta1.setDisable(true);
			btnCadastrar1.setDisable(true);
		}else{
			warnings.setVisible(true);
			warnings.setTextFill(Paint.valueOf("#ff0000"));
			warnings.setText("Altere algum campo para atualizar o alerta!");
		}
	}
	@FXML
	protected void handleBtnAlertarAction(ActionEvent event) throws IOException {
		this.atualizaFrame("FXMLCadastroAlerta.fxml", event);
	}
	@FXML
	protected void handleBtnMenuAction(ActionEvent event) throws IOException {
		this.atualizaFrame("FXMLAlertas.fxml", event);
	}
	@FXML
	protected void handleBtnPerfilAction(ActionEvent event) throws IOException {
		this.atualizaFrame("FXMLPerfil.fxml", event);
	}
	@FXML
	private void handleBtnRastreamentoAction(ActionEvent event) throws IOException{
		this.atualizaFrame("FXMLRastreamento.fxml", event);
	}
	@FXML
	protected void handleBtnSairAction(ActionEvent evente) throws IOException {
		this.setUser(null);
		Stage stage = (Stage) ((Node) evente.getSource()).getScene().getWindow();
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Login.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 600, 400);
		stage.setScene(scene);
		stage.show();
	}
	private void atualizaFrame(String frame, ActionEvent evente) throws IOException {;
		stage = (Stage) ((Node) evente.getSource()).getScene().getWindow();
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(frame));
		Scene scene = new Scene(fxmlLoader.load(), 600, 500);
		stage.setScene(scene);
		stage.show();
	}

}