package org.ektorp;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Replication response doc is not very well documented in the CouchDB reference...
 *
 * @author henrik lundgren
 *
 * http://docs.couchdb.org/en/stable/replication/protocol.html
 */
public class ReplicationStatus extends Status implements Serializable {

	private static final long serialVersionUID = 6617269292660336903L;

	@JsonProperty("ok")
	boolean ok;

	@JsonProperty("no_changes")
	boolean noChanges;

	@JsonProperty("session_id")
	String sessionId;

	@JsonProperty("_local_id")
	String id;

	@JsonProperty("source_last_seq")
	JsonNode sourceLastSequence;

	@JsonProperty("history")
	List<History> history;

	private Map<String, Object> unknownFields = new HashMap<>();

	public boolean isOk() {
		return ok;
	}

	public boolean isNoChanges() {
		return noChanges;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getId() {
        return id;
    }

	public String getSourceLastSequence() {
		return sourceLastSequence != null ? sourceLastSequence.asText() : null;
	}

	public JsonNode getSourceLastSequenceAsNode() {
		return sourceLastSequence;
	}

	public List<History> getHistory() {
		return history;
	}

	private Map<String, Object> unknown() {
		if (unknownFields == null) {
			unknownFields = new HashMap<>();
		}
		return unknownFields;
	}

	@JsonAnySetter
	public void setUnknown(final String key, final Object value) {
		unknown().put(key, value);
	}

	public Object getField(final String key) {
		return unknown().get(key);
	}

	// https://stackoverflow.com/questions/4861228/how-to-handle-a-findbugs-non-transient-non-serializable-instance-field-in-seria
	private void writeObject(final ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
	}

	private void readObject(final ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
	}

	public static class History {

		private Map<String, Object> unknownFields = new HashMap<>();

		@JsonProperty("session_id")
		String sessionId;

		@JsonProperty("start_time")
		String startTime;

		@JsonProperty("end_time")
		String endTime;

		@JsonProperty("start_last_seq")
		JsonNode startLastSeq;

		@JsonProperty("end_last_seq")
		JsonNode endLastSeq;

		@JsonProperty("missing_checked")
		int missingChecked;

		@JsonProperty("missing_found")
		int missingFound;

		@JsonProperty("docs_read")
		int docsRead;

		@JsonProperty("docs_written")
		int docsWritten;

		@JsonProperty("doc_write_failures")
		int docWriteFailures;

		@JsonProperty("recorded_seq")
		JsonNode recordedSeq;

		public String getRecordedSeq() {
			return recordedSeq != null ? recordedSeq.asText() : null;
		}

		public String getSessionId() {
			return sessionId;
		}

		public String getStartTime() {
			return startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public String getStartLastSeq() {
			return startLastSeq != null ? startLastSeq.asText() : null;
		}

		public JsonNode getStartLastSeqAsNode() {
			return startLastSeq;
		}

		public String getEndLastSeq() {
			return endLastSeq != null ? endLastSeq.asText() : null;
		}

		public JsonNode getEndLastSeqAsNode() {
			return endLastSeq;
		}

		public int getMissingChecked() {
			return missingChecked;
		}

		public int getMissingFound() {
			return missingFound;
		}

		public int getDocsRead() {
			return docsRead;
		}

		public int getDocsWritten() {
			return docsWritten;
		}

		public int getDocWriteFailures() {
			return docWriteFailures;
		}

		private Map<String, Object> unknown() {
			if (unknownFields == null) {
				unknownFields = new HashMap<>();
			}
			return unknownFields;
		}

		@JsonAnySetter
		public void setUnknown(String key, Object value) {
			unknown().put(key, value);
		}

		public Object getField(String key) {
			return unknown().get(key);
		}

        @Override
        public String toString() {
            return "Replication history: " + getSessionId() + ", " + getRecordedSeq();
        }
	}

}
