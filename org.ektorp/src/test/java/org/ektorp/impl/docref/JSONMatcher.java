package org.ektorp.impl.docref;

import org.ektorp.util.*;
import org.junit.Assert;
import org.mockito.*;

public class JSONMatcher implements ArgumentMatcher<String> {

	private final String expectedJSON;

	public JSONMatcher(String expectedJSON) {
		super();
		this.expectedJSON = expectedJSON;
	}

	public boolean matches(String actualJSON) {
		if(!JSONComparator.areEqual(expectedJSON, actualJSON)) {
			Assert.assertEquals(expectedJSON, actualJSON);
			return false;
		}
		return true;

	}

}
