package CustomerImplemantationsTests;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CustomerContracts.ICustomer;
import CustomerContracts.ACustomerExceptions.CriticalError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerImplementations.Customer;
import CustomerImplementations.CustomerDefs;
import UtilsContracts.IClientRequestHandler;

@RunWith(MockitoJUnitRunner.class)

public class LogoutTest {
	private ICustomer customer;

	@Mock
	private IClientRequestHandler clientRequestHandler;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
		customer = new Customer(clientRequestHandler);
	}
	
	@Test
	public void logoutSuccessfulTest() {
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond((new CommandWrapper(CustomerDefs.loginCommandSenderId,
						CommandDescriptor.LOGOUT).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_OK).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		
		try {
			customer.logout();
		} catch (CustomerNotConnected | CriticalError e) {
			e.printStackTrace();
			
			fail();
		}
	}
	
	@Test
	public void logoutCriticalErrorTest() {
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond((new CommandWrapper(CustomerDefs.loginCommandSenderId,
						CommandDescriptor.LOGOUT).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_ERR).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		
		try {
			customer.logout();
		} catch (CustomerNotConnected e) {
			e.printStackTrace();
			
			fail();
		} catch (CriticalError __) {
			/* success */
		}
	}
	
	@Test
	public void logoutCustomerNotConnectedTest() {
		try {
			Mockito.when(
				clientRequestHandler.sendRequestWithRespond((new CommandWrapper(CustomerDefs.loginCommandSenderId,
						CommandDescriptor.LOGOUT).serialize())))
				.thenReturn(new CommandWrapper(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED).serialize());
		} catch (IOException ¢) {
			¢.printStackTrace();
			fail();
		}
		
		try {
			customer.logout();
		} catch (CriticalError e) {
			e.printStackTrace();
			
			fail();
		} catch (CustomerNotConnected __) {
			/* success */
		}
	}
}