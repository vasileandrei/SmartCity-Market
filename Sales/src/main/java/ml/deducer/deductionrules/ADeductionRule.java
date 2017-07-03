package ml.deducer.deductionrules;

import java.util.Set;

import api.preferences.SalesPreferences;
import ml.common.property.AProperty;

/**
 * This is abstract of any Deduction rule
 * 
 * @author noam
 * 
 */
public abstract class ADeductionRule {

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		boolean isComparedWithAnyRuleQualifier = obj.getClass() == AnyDeductionRule.class;
		if (isComparedWithAnyRuleQualifier){
			return true;
		}

		return (getClass() == obj.getClass());
	}
	
	public abstract Set<? extends AProperty> deduceProperties(SalesPreferences preferences, Set<AProperty> properties);
	
	public abstract boolean canDeduceProperty(AProperty property);
	
	public abstract Set<AProperty> whatNeedToDeduceProperty(AProperty property);
	
}
	
