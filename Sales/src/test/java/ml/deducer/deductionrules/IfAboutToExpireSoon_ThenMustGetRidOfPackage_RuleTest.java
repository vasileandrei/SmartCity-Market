package ml.deducer.deductionrules;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import BasicCommonClasses.PlaceInMarket;
import ml.common.property.AProperty;
import ml.common.property.basicproperties.storestatistics.AboutToExpireSoonStorePackageProperty;
import ml.common.property.deducedproperties.MustGetRidOfPackageProperty;
import testmocks.DBMock;
import testmocks.StorePackageMock;

public class IfAboutToExpireSoon_ThenMustGetRidOfPackage_RuleTest {

	@Test
	public void testDeduceProperties() {
		AboutToExpireSoonStorePackageProperty property =
				new AboutToExpireSoonStorePackageProperty(new StorePackageMock(1, 1, LocalDate.now().plusDays(2), PlaceInMarket.STORE));
		
		Set<AProperty> propertySet = new HashSet<>();
		propertySet.add(property);
		
		Set<? extends AProperty> resultProperty = 
				new IfAboutToExpireSoon_ThenMustGetRidOfPackage_Rule().deduceProperties(DBMock.getSalePref(), propertySet);
		
		assertEquals(1, resultProperty.size());
		assert resultProperty.contains(new MustGetRidOfPackageProperty(property.getStorePackage(),
				1 - 2.0 / AboutToExpireSoonStorePackageProperty.threshold));
	}

}
