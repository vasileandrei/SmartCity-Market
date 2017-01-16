package EmployeeGui;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import com.google.common.eventbus.Subscribe;

import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.Location;
import BasicCommonClasses.PlaceInMarket;
import BasicCommonClasses.ProductPackage;
import BasicCommonClasses.SmartCode;
import EmployeeCommon.TempWorkerPassingData;
import EmployeeContracts.IWorker;
import EmployeeDefs.AEmployeeException;
import EmployeeDefs.EmployeeGuiDefs;
import GuiUtils.AbstractApplicationScreen;
import GuiUtils.DialogMessagesService;
import GuiUtils.RadioButtonEnabler;
import SMExceptions.SMException;
import UtilsContracts.BarcodeEventHandlerDiConfigurator;
import UtilsContracts.BarcodeScanEvent;
import UtilsContracts.IBarcodeEventHandler;
import UtilsImplementations.BarcodeEventHandler;
import UtilsImplementations.InjectionFactory;
//import UtilsImplementations.BarcodeScanner;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * WorkerMenuScreen - Controller for menu screen which holds the operations
 * available for worker.
 * 
 * @author idan atias
 * @author Shimon Azulay
 * @since 2016-12-27
 */

public class WorkerMenuScreen implements Initializable {

	// Main screen panes
	@FXML
	GridPane workerMenuScreenPane;

	@FXML
	GridPane quickProductDetailsPane;

	@FXML
	HBox addPackageToWarehouseParametersPane;

	@FXML
	TextField barcodeTextField;

	@FXML
	Button showMoreDetailsButton;

	@FXML
	TextArea successLogArea;
	
	@FXML
	Label smartCodeValLabel;

	@FXML
	Label productNameValLabel;

	@FXML
	Label expirationDateValLabel;

	@FXML
	Label amoutInStoreValLabel;

	@FXML
	Label AmountInWarehouseValLabel;

	@FXML
	Label expirationDateLabel;

	@FXML
	CheckBox printTheSmartCodeCheckBox;

	@FXML
	Button runTheOperationButton;

	@FXML
	Spinner<Integer> editPackagesAmountSpinner;

	@FXML
	DatePicker datePicker;

	@FXML
	RadioButton printSmartCodeRadioButton;

	@FXML
	RadioButton addPackageToStoreRadioButton;

	@FXML
	RadioButton removePackageFromStoreRadioButton;

	@FXML
	RadioButton removePackageFromWarhouseRadioButton;

	@FXML
	RadioButton addPakageToWarhouseRadioButton;

	RadioButtonEnabler radioButtonContainer = new RadioButtonEnabler();

	Stage primeStage = EmployeeApplicationScreen.stage;

	IWorker worker;

	CatalogProduct catalogProduct;
	
	IBarcodeEventHandler barcodeEventHandler;

	int amountInStore = -1;

	int amountInWarehouse = -1;

	@Override
	public void initialize(URL location, ResourceBundle __) {
		AbstractApplicationScreen.fadeTransition(workerMenuScreenPane);
		barcodeEventHandler = InjectionFactory.getInstance(BarcodeEventHandler.class,
				new BarcodeEventHandlerDiConfigurator());
		// TODO enable this after solve singleton
		//barcodeEventHandler.register(this);
		barcodeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			addProductParametersToQuickView("N/A", "N/A", "N/A", "N/A", "N/A");
			amountInWarehouse = amountInStore = -1;
			enableRunTheOperationButton();

		});
		datePicker.setValue(LocalDate.now());
		worker = TempWorkerPassingData.worker;

		enableRunTheOperationButton();
		addPackageToWarehouseButtonPressedCheck();

		// defining behavior when stage/window is closed.
		primeStage.setOnCloseRequest(event -> {
			try {
				if (worker.isLoggedIn())
					worker.logout();
			} catch (SMException e) {
				if (e instanceof AEmployeeException.EmployeeNotConnected)
					return;
				EmployeeGuiExeptionHandler.handle(e);
			}
		});

		//setting success log listener
		successLogArea.textProperty().addListener(new ChangeListener<Object>(){
		    @Override
			public void changed(ObservableValue<?> __, Object oldValue,
		            Object newValue) {
		    	successLogArea.setScrollTop(Double.MAX_VALUE); //this will scroll to the bottom
		    }
		});

		radioButtonContainer.addRadioButtons((Arrays.asList((new RadioButton[] { printSmartCodeRadioButton,
				addPackageToStoreRadioButton, removePackageFromStoreRadioButton, removePackageFromWarhouseRadioButton,
				addPakageToWarhouseRadioButton }))));

	}
	
	private void printToSuccessLog(String msg) {
		successLogArea.appendText(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss").format(new Date()) + " :: " + msg + "\n");
	}

	private void addProductParametersToQuickView(String productName, String productBarcode,
			String productExpirationDate, String amountInStore, String amountInWarehouse) {
		smartCodeValLabel.setText(productBarcode);

		productNameValLabel.setText(productName);

		// TODO should get expiration date from the smart code
		expirationDateValLabel.setText(productExpirationDate);

		// TODO get amounts from server
		amoutInStoreValLabel.setText(amountInStore);
		AmountInWarehouseValLabel.setText(amountInWarehouse);

	}

	private void enableRunTheOperationButton() {
		if (addPakageToWarhouseRadioButton.isSelected()) {
			runTheOperationButton.setDisable("".equals(barcodeTextField.getText()));
			runTheOperationButton.setTooltip(
					new Tooltip(!"".equals(barcodeTextField.getText()) ? "" : EmployeeGuiDefs.typeBarcode));
		} else {
			showMoreDetailsButton.setDisable("N/A".equals(smartCodeValLabel.getText()));
			if (removePackageFromStoreRadioButton.isSelected()) {
				runTheOperationButton.setDisable(amountInStore < editPackagesAmountSpinner.getValue());
				runTheOperationButton.setTooltip(new Tooltip(EmployeeGuiDefs.notEnoughAmountInStore));
				return;
			}
			if (removePackageFromWarhouseRadioButton.isSelected()) {
				runTheOperationButton.setDisable(amountInWarehouse < editPackagesAmountSpinner.getValue());
				runTheOperationButton.setTooltip(new Tooltip(EmployeeGuiDefs.notEnoughAmountInWarehouse));
				return;
			}
			if (addPackageToStoreRadioButton.isSelected()) {
				runTheOperationButton.setDisable(amountInWarehouse < editPackagesAmountSpinner.getValue());
				runTheOperationButton.setTooltip(new Tooltip(EmployeeGuiDefs.notEnoughAmountInStore));
				return;
			}
			runTheOperationButton.setDisable("N/A".equals(smartCodeValLabel.getText()));
			runTheOperationButton.setTooltip(
					new Tooltip(!"N/A".equals(smartCodeValLabel.getText()) ? "" : EmployeeGuiDefs.typeSmartCode));
		}
	}

	@FXML
	private void searchBarcodePressed(ActionEvent __) {
		if (addPakageToWarhouseRadioButton.isSelected())
			return;
		catalogProduct = null;
		try {
			catalogProduct = worker.viewProductFromCatalog(Long.parseLong(barcodeTextField.getText()));
			SmartCode smartcode = new SmartCode(Long.parseLong(barcodeTextField.getText()), LocalDate.now());
			amountInStore = worker
					.getProductPackageAmount(new ProductPackage(smartcode, 0, new Location(0, 0, PlaceInMarket.STORE)));
			amountInWarehouse = worker.getProductPackageAmount(
					new ProductPackage(smartcode, 0, new Location(0, 0, PlaceInMarket.WAREHOUSE)));
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
			return;
		}
		if (catalogProduct != null)
			addProductParametersToQuickView(barcodeTextField.getText(), catalogProduct.getName(), "N/A",
					Integer.toString(amountInStore), Integer.toString(amountInWarehouse));
		runTheOperationButton.setDisable(false);
	}

	@FXML
	private void radioButtonHandling(ActionEvent ¢) {
		radioButtonContainer.selectRadioButton((RadioButton) ¢.getSource());
		addPackageToWarehouseButtonPressedCheck();
		enableRunTheOperationButton();
	}

	@FXML
	private void runTheOperationButtonPressed(ActionEvent __) {

		SmartCode smartcode = new SmartCode(Long.parseLong(barcodeTextField.getText()), datePicker.getValue());
		/*TODO - find what's wrong with using getValue on the spinner. it keeps throwing exception about not
		 * being able to case int to doulbe (or the opposite)
		 */

		//int amountVal = editPackagesAmountSpinner.getValue();
		int amountVal = 1;

		try {
			if (addPakageToWarhouseRadioButton.isSelected()){
				
				//init
				Location loc = new Location(0, 0, PlaceInMarket.WAREHOUSE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
				
				//exec
				worker.addProductToWarehouse(pp);
				
				printToSuccessLog(
						("Added (" + amountVal + ") " + "product packages (" + pp + ") to warehouse"));
			
			} else if (addPackageToStoreRadioButton.isSelected()) {
				
				//init
				Location loc = new Location(0, 0, PlaceInMarket.STORE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
				
				//exec
				worker.placeProductPackageOnShelves(pp);
				
				printToSuccessLog(("Added (" + amountVal + ") " + "product packages (" + pp + ") to store"));
			
			} else if (removePackageFromStoreRadioButton.isSelected()) {
				
				//init
				Location loc = new Location(0, 0, PlaceInMarket.STORE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
				
				//exec
				worker.removeProductPackageFromStore(pp);
				
				printToSuccessLog(
						("Removed (" + amountVal + ") " + "product packages (" + pp + ") from store"));
			
			} else if (removePackageFromWarhouseRadioButton.isSelected()) {
				Location loc = new Location(0, 0, PlaceInMarket.WAREHOUSE);
				ProductPackage pp = new ProductPackage(smartcode, amountVal, loc);
				worker.removeProductPackageFromStore(pp);
				printToSuccessLog(("Removed (" + amountVal + ") " + "product packages (" + pp + ") from warehouse"));
			}
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
	}

	@FXML
	private void logoutButtonPressed(ActionEvent __) {
		try {
			worker.logout();
		} catch (SMException e) {
			EmployeeGuiExeptionHandler.handle(e);
		}
		// TODO enable this after solve singleton
		// barcodeEventHandler.unregister(this);
		AbstractApplicationScreen.setScene("/EmployeeLoginScreen/EmployeeLoginScreen.fxml");
	}

	@FXML
	private void showMoreDetailsButtonPressed(ActionEvent __) {
		if (catalogProduct != null)
			DialogMessagesService.showInfoDialog(catalogProduct.getName(),
					"Description: " + catalogProduct.getDescription(),
					"Barcode: " + catalogProduct.getBarcode() + "\n" + "Manufacturer: "
							+ catalogProduct.getManufacturer().getName() + "\n" + "Price: "
							+ catalogProduct.getPrice());

	}

	// Enable Disable Show more info, date picker. Change prompt text
	private void addPackageToWarehouseButtonPressedCheck() {
		if (!addPakageToWarhouseRadioButton.isSelected()) {
			barcodeTextField.setPromptText(EmployeeGuiDefs.smartCodePrompt);
			disableAddPackageToWarehouseButtonParameters(true);
		} else {
			barcodeTextField.setPromptText(EmployeeGuiDefs.barcodeCodePrompt);
			disableAddPackageToWarehouseButtonParameters(false);
		}
	}

	private void disableAddPackageToWarehouseButtonParameters(boolean disable) {
		addPackageToWarehouseParametersPane.setDisable(disable);
		quickProductDetailsPane.setDisable(!disable);

	}
	
	@Subscribe
	public void barcodeScanned(BarcodeScanEvent ¢) {
		barcodeTextField.setText(¢.getBarcode());
		enableRunTheOperationButton();
		searchBarcodePressed(null);
	}

}
