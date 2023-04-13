package me.elephantsuite.response.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.elephantsuite.answers.ElephantAnswer;

public class AnswerSerializer extends JsonSerializer<ElephantAnswer> {
	/**
	 * Method that can be called to ask implementation to serialize
	 * values of type this serializer handles.
	 *
	 * @param value       Value to serialize; can <b>not</b> be null.
	 * @param gen         Generator used to output resulting Json content
	 * @param serializers Provider that can be used to get serializers for
	 *                    serializing Objects value contains, if any.
	 */
	@Override
	public void serialize(ElephantAnswer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
			gen.writeNumberField("id", value.getId());
			gen.writeStringField("title", value.getTitle());
			gen.writeStringField("description", value.getDescription());
			gen.writeBooleanField("answered", value.isAnswered());
			gen.writeNumberField("numberOfLikes", value.getNumberOfLikes());
			gen.writeNumberField("authorId", value.getUser().getId());
			gen.writeStringField("authorName", value.getUser().getFullName());
			gen.writeNumberField("authorPfpId", value.getUser().getPfpId());
			gen.writeFieldName("comments");
			gen.writeStartArray();
				value.getComments().forEach(comment -> {
					try {
						gen.writeObject(comment);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			gen.writeEndArray();
			gen.writeFieldName("tags");
			gen.writeStartArray();
				value.getTags().forEach(integer -> {
					try {
						gen.writeNumber(integer);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				});
			gen.writeEndArray();
			gen.writeStringField("created", value.getCreated().toString());
			gen.writeStringField("lastUpdated", value.getLastUpdated().toString());
		gen.writeEndObject();
	}
}
