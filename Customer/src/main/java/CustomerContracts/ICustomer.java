package CustomerContracts;

import java.util.HashMap;

import BasicCommonClasses.CartProduct;
import BasicCommonClasses.CatalogProduct;
import BasicCommonClasses.CustomerProfile;
import BasicCommonClasses.SmartCode;
import CustomerContracts.ACustomerExceptions.AmountBiggerThanAvailable;
import CustomerContracts.ACustomerExceptions.AuthenticationError;
import CustomerContracts.ACustomerExceptions.CustomerNotConnected;
import CustomerContracts.ACustomerExceptions.CriticalError;
import CustomerContracts.ACustomerExceptions.GroceryListIsEmpty;
import CustomerContracts.ACustomerExceptions.InvalidParameter;
import CustomerContracts.ACustomerExceptions.ProductCatalogDoesNotExist;
import CustomerContracts.ACustomerExceptions.ProductNotInCart;
import CustomerContracts.ACustomerExceptions.ProductPackageDoesNotExist;
import CustomerContracts.ACustomerExceptions.UsernameAlreadyExists;

/**
 * ICustomer - This interface is the contract for Customer Type.
 *
 * @author Lior Ben Ami
 * @since 2017-01-04
 */
public interface ICustomer {
	/**
	 * getId - returns the customer's id
	 * @return int
	 */
	int getId();

	/**
	 * getCartProductCache - returns the current CartProduct Cache.
	 * @return HashMap<SmartCode, CartProduct> 
	 */
	HashMap<Long, CartProduct> getCartProductCache();
	
	/**
	 * login - the customer login to the server and gets it's own id;
	 * @throws CriticalError 
	 * @throws AuthenticationError
	 */
	void login(String username, String password) throws CriticalError, AuthenticationError;
	
	/**
	 * logout - the customer logout from  the server. To use in the end of the shopping.
	 * @throws CustomerNotConnected 
	 * @throws CriticalError 
	 */
	void logout() throws CustomerNotConnected, CriticalError;
	
	/**
	 * resume - saves the data of the customer from the server (to use in case of collapse)
	 * @param int _id
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws ProductCatalogDoesNotExist 
	 */
	void resume(int _id) throws CriticalError, CustomerNotConnected, ProductCatalogDoesNotExist;
	
	/**
	 * addPtoductToCart - Adds product with amount to the customer
	 * 
	 * @param SmartCode c
	 * @param int amount
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws ProductPackageDoesNotExist 
	 * @throws AmountBiggerThanAvailable 
	 * @throws ProductCatalogDoesNotExist 
	 */
	void addProductToCart(SmartCode c, int amount) throws CriticalError, CustomerNotConnected, AmountBiggerThanAvailable, ProductPackageDoesNotExist, ProductCatalogDoesNotExist;
	
	/**
	 * returnProductToShelf - removes product with amount from the customer
	 * 
	 * @param SmartCode c 
	 * @param  int amount
	 * @throws ProductNotInCart
	 * @throws AmountBiggerThanAvailable 
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws ProductPackageDoesNotExist 
	 */
	void returnProductToShelf(SmartCode c, int amount) throws CriticalError, CustomerNotConnected, ProductNotInCart, AmountBiggerThanAvailable, ProductPackageDoesNotExist;
	
	/**
	 * getTotalSum - returns the total sum of the shopping
	 * @return Double
	 */
	Double getTotalSum(); 
	
	/**
	 * getCartProductsNum - returns the number of the CarProducts in the customer
	 * @return Integer
	 */
	Integer getCartProductsNum();
	
	/**
	 * checkOutGroceryList - returns the finale total sum of the shopping and initialize grocery list
	 * @return Double
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws GroceryListIsEmpty 
	 */
	Double checkOutGroceryList() throws CriticalError, CustomerNotConnected, GroceryListIsEmpty;

	CartProduct getCartProduct(SmartCode c);

	/**
	 * @throws CriticalError 
	 * @throws CustomerNotConnected 
	 * @throws ProductCatalogDoesNotExist
	 */
	CatalogProduct viewCatalogProduct(SmartCode c) throws CriticalError, CustomerNotConnected, ProductCatalogDoesNotExist;
	
	/**
	 * @throws ProductNotInCart 
	 * @throws CriticalError 
	 * 
	 */
	void removeAllItemsOfCartProduct(SmartCode c) throws ProductNotInCart, CriticalError;
	
	/**
	 * @throws InvalidParameter
	 * @throws ProductNotInCart 
	 * @throws CriticalError 
	 * 
	 */
	void registerNewCustomer(CustomerProfile p) throws CriticalError, InvalidParameter, UsernameAlreadyExists;
}