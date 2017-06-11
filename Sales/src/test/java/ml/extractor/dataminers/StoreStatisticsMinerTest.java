package ml.extractor.dataminers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProduct;
import testmocks.DBMock;
import testmocks.GroceryListMock;
import testmocks.GroceryPackageMock;
import testmocks.ProductMock;
import testmocks.StorePackageMock;

public class StoreStatisticsMinerTest {

	private static List<GroceryListMock> history = new ArrayList<>();
	private static List<StorePackageMock> stock = new ArrayList<>();
	private static StoreData<ProductMock> sd = new StoreData<ProductMock>(history, stock);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		for (long i=1; i <= DBMock.NUM_OF_PRODUCTS; i++)
			history.add(new GroceryListMock("alice").addProdcut(DBMock.getProduct(i)));
		
		for (long i=1; i <= 50; i++)
			history.add(new GroceryListMock("bob").addProdcut(DBMock.getProduct(i)));
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test50MostPopularProperty() {
		Set<ABasicProperty> result = new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryPackageMock(DBMock.getProduct(1)))
				.extractProperties();
		
		long numOfRightAmount = result.stream()
				.filter(p -> p instanceof MostPopularProduct & ((MostPopularProduct)p).getAmount() == 2).count();
		
		for (long i=1; i <= 50; i++)
			assertTrue("validate product: " + i, result.contains(new MostPopularProduct(2, DBMock.getProduct(i))));
		
		assertEquals(50, numOfRightAmount);
		
	}

	@Test
	public void testStoreStatisticsMiner() {
		new StoreStatisticsMiner(DBMock.getInputPref(), sd, new GroceryPackageMock(DBMock.getProduct(1)));
	}

}
