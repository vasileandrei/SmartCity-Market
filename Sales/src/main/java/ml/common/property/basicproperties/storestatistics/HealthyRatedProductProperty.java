package ml.common.property.basicproperties.storestatistics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import api.contracts.IProduct;
import ml.common.property.basicproperties.ABasicProperty;
import ml.deducer.deductionrules.ADeductionRule;

/**
 * @author idan atias
 *
 * @since Jun 24, 2017
 * 
 * this class represents healthy rated product property
 */
public class HealthyRatedProductProperty extends ABasicProperty {

	private static List<String> healthyRatedIngredientsNames = new ArrayList<>(Arrays.asList("rice", "corn", "beans"));
	private static int intersectionSizeToBeRatedHealthy = 1;
	
	IProduct product;
	
	public HealthyRatedProductProperty(IProduct product) {
		this.product = product;
	}
	
	public HealthyRatedProductProperty(IProduct product, ADeductionRule rule) {
		super(rule);
		this.product = product;
	}
	
	public static boolean isProductRatedHealthy(IProduct product){
		Set<String> productIngredientsNames = 
				product.getIngredients()
				.stream()
				.map(i -> i.getName())
				.collect(Collectors.toSet());

		Set<String> intersection = new HashSet<String>(healthyRatedIngredientsNames);
		intersection.retainAll(productIngredientsNames);
		return intersection.size() >= intersectionSizeToBeRatedHealthy;
	}

	public IProduct getProduct(){
		return product;
	}
}