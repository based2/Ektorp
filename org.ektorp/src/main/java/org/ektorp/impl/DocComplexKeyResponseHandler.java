package org.ektorp.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ektorp.ComplexKey;
import org.ektorp.DbAccessException;
import org.ektorp.http.HttpResponse;
import org.ektorp.http.StdResponseHandler;

/**
 *
 * @author henrik lundgren
 *
 */
public class DocComplexKeyResponseHandler extends StdResponseHandler<List<ComplexKey>> {

	private final JsonFactory jsonFactory;

	public DocComplexKeyResponseHandler(ObjectMapper om) {
		jsonFactory = om.getFactory();
	}

    /**
     * The token is required to be on the START_ARRAY value for rows.
     * @param jp
     * @param result
     * @return The results found in the rows object
     */
    private List<ComplexKey> parseRows(JsonParser jp, List<ComplexKey> result) throws IOException {
        while(jp.nextToken() == JsonToken.START_OBJECT) {
            while(jp.nextToken() == JsonToken.FIELD_NAME)
            {
                String fieldName = jp.getCurrentName();

                if ("key".equals(fieldName)) {
                    if (jp.nextToken() != JsonToken.START_ARRAY) {
                        throw new DbAccessException("Complex keys are expected");
                    }
                    JsonToken t;
                    List<Object> key = new LinkedList<>();
                    while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
                        if (t == JsonToken.VALUE_NULL) {
                            key.add(null);
                        }else if (t.isNumeric()) {
                            key.add(jp.getNumberValue());
                        } else if (t.isScalarValue()) {
                            key.add(jp.getText());
                        }
                    }
                    result.add(ComplexKey.of(key.toArray(new Object[] {key.size()})));
                } else {
                    jp.nextToken();
                    jp.skipChildren();
                }
            }
        }
        return result;
    }

    @Override
    public List<ComplexKey> success(HttpResponse hr) throws Exception {
        JsonParser jp = jsonFactory.createParser(hr.getContent());
        if (jp.nextToken() != JsonToken.START_OBJECT) {
            throw new DbAccessException("Expected data to start with an Object");
        }

        List<ComplexKey> result = null;

        while (jp.nextToken() != null) {
            if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                String fieldName = jp.getCurrentName();
                if ("total_rows".equals(fieldName)) {
                    if (result != null) {
                        throw new DbAccessException("Two total_rows were provided.");
                    }

                    jp.nextToken();
                    result = new ArrayList<>(jp.getIntValue());
                } else if ("rows".equals(fieldName)) {
                    if (result == null) {
                        throw new DbAccessException("total_rows is required to be included in value before rows");
                    }

                    if (jp.nextToken() != JsonToken.START_ARRAY) {
                        throw new DbAccessException("rows's value must be an array");
                    }

                    result = parseRows(jp, result);
                } else {
                    jp.skipChildren();
                }
            }
        }

        return result;
    }
}
