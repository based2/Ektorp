package org.ektorp.impl.docref;

import java.util.*;

import org.ektorp.docref.*;
import org.ektorp.support.*;

@SuppressWarnings("serial")
public class NoCascadeLounge extends CouchDbDocument {

	@DocumentReferences(fetch = FetchType.EAGER, backReference="loungeId", cascade = CascadeType.NONE)
	private Set<Person> seatedPeople = new LinkedHashSet<>();
	private String color;
	
	public Set<Person> getSeatedPeople() {
		return seatedPeople;
	}
	
	public void setSeatedPeople(Set<Person> seatedPeople) {
		this.seatedPeople = seatedPeople;
	}
	
	public void sitDown(Person person) {
		person.setLoungeId(getId());
		getSeatedPeople().add(person);
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

}
