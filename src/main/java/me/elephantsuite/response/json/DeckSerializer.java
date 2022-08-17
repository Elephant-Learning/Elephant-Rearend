package me.elephantsuite.response.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.elephantsuite.deck.Deck;

public class DeckSerializer extends JsonSerializer<Deck> {
	/**
	 * Method that can be called to ask implementation to serialize
	 * values of type this serializer handles.
	 *
	 * @param value       Value to serialize; can <b>not</b> be null.
	 * @param gen         Generator used to output resulting Json content
	 * @param serializers Provider that can be used to get serializers for
	 */
	@Override
	public void serialize(Deck value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

		gen.writeStartObject();
			gen.writeNumberField("id", value.getId());
			gen.writeNumberField("numberOfLikes", value.getNumberOfLikes());
			gen.writeStringField("visibility", value.getVisibility().toString());
			gen.writeNumberField("authorId", value.getAuthor().getId());
			gen.writeStringField("name", value.getName());
			gen.writeStringField("created", value.getCreated().toString());
			gen.writeFieldName("cards");
			gen.writeStartArray();
				value.getCards().forEach(card -> {
					try {
						gen.writeObject(card);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			gen.writeEndArray();
			gen.writeFieldName("sharedUsers");
			gen.writeStartArray();
				value.getSharedUsersIds().forEach(user -> {
					try {
						gen.writeNumber(user);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			gen.writeEndArray();
		gen.writeEndObject();
	}
}
