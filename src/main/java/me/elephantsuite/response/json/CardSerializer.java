package me.elephantsuite.response.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.elephantsuite.deck.card.Card;

public class CardSerializer extends JsonSerializer<Card> {
	/**
	 * Method that can be called to ask implementation to serialize
	 * values of type this serializer handles.
	 *
	 * @param value       Value to serialize; can <b>not</b> be null.
	 * @param gen         Generator used to output resulting Json content
	 * @param serializers Provider that can be used to get serializers for
	 */
	@Override
	public void serialize(Card value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

		gen.writeStartObject();
			gen.writeNumberField("id", value.getId());
			if (value.getDeck() == null) {
				gen.writeNullField("deckId");
			} else {
				gen.writeNumberField("deckId", value.getDeck().getId());
			}
			gen.writeStringField("deckName", value.getDeckName());
			gen.writeStringField("term", value.getTerm());
			gen.writeFieldName("definitions");
			gen.writeStartArray();
				value.getDefinitions().forEach(s -> {
					try {
						gen.writeString(s);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			gen.writeEndArray();
		gen.writeEndObject();
	}
}
