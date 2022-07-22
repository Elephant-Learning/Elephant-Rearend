package me.elephantsuite.deck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@Transactional
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class DefinitionList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ElementCollection
	private List<String> definitions = new ArrayList<>();

	public DefinitionList(List<String> definitions) {
		this.definitions = definitions;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DefinitionList list) {
			return list.definitions.equals(this.definitions);
		}

		return false;
	}
}
