package org.ektorp.impl;

import org.ektorp.ReplicationTask;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StdReplicationTask extends StdActiveTask implements ReplicationTask {
	private String replicationId;
    private String replicationDocumentId;
    private boolean isContinuous;
    private long writeFailures;
	private long changesPending;
    private long totalReads;
    private long totalWrites;
    private long totalMissingRevisions;
    private long totalRevisionsChecked;
    private String sourceDatabase;
    private String targetDatabase;
    private String sourceSequenceId;
    private String checkpointedSourceSequenceId;
    private long checkpointInterval;

    /*
    {
    	"node":"couchdb@127.0.0.1",
		"pid":"<0.17238.0>",
		"changes_pending":0,
		"checkpoint_interval":30000,
		"checkpointed_source_seq":"36522-g1AAAADweJzLYWBgYM5gTmEQTM4vTc5ISXIwNDLXMwBCwxygFFMiQ1JoaGhIVgZzEgODflkuUIzdzCTVKDnNEJsePCaBzAnNY2FYuWrVKohxBv1g4wwNDZON0yxJNA5oEtCgVUDqPxBADNRbCjbQwjLZ0NDACJvWLADImD3x",
		"continuous":true,
		"database":null,
		"doc_id":null,
		"doc_write_failures":2,
		"docs_read":8537,
		"docs_written":8535,
		"missing_revisions_found":8537,
		"replication_id":"6c1a34e8b789a5618934ef984770d16d+continuous",
		"revisions_checked":8537,
		"source":"http://127.0.0.1:5984/icure-patient/",
		"source_seq":"36522-g1AAAADweJzLYWBgYM5gTmEQTM4vTc5ISXIwNDLXMwBCwxygFFMiQ1JoaGhIVgZzEgODflkuUIzdzCTVKDnNEJsePCaBzAnNY2FYuWrVKohxBv1g4wwNDZON0yxJNA5oEtCgVUDqPxBADNRbCjbQwjLZ0NDACJvWLADImD3x",
		"started_on":1483528117,
		"target":"https://ad-c7a2fd59-1663-4e3a-a7d6-4f67ebc49b4f:*****@couchdb.icure.cloud:443/icure-ad-c7a2fd59-1663-4e3a-a7d6-4f67ebc49b4f-patient/",
		"through_seq":"36522-g1AAAADweJzLYWBgYM5gTmEQTM4vTc5ISXIwNDLXMwBCwxygFFMiQ1JoaGhIVgZzEgODflkuUIzdzCTVKDnNEJsePCaBzAnNY2FYuWrVKohxBv1g4wwNDZON0yxJNA5oEtCgVUDqPxBADNRbCjbQwjLZ0NDACJvWLADImD3x",
		"type":"replication",
		"updated_on":1483529048,
		"user":"icure"
	}
	*/

    @Override
    public String getReplicationId() {
        return replicationId;
    }

    @JsonProperty(required = false, value = "replication_id")
    public void setReplicationId(String replicationId) {
        this.replicationId = replicationId;
    }

    @Override
    public String getReplicationDocumentId() {
        return replicationDocumentId;
    }

    @JsonProperty(required = false, value = "doc_id")
    public void setReplicationDocumentId(String replicationDocumentId) {
        this.replicationDocumentId = replicationDocumentId;
    }

    @Override
    public boolean isContinuous() {
        return isContinuous;
    }

    @JsonProperty(required = false, value = "continuous")
    public void setContinuous(boolean isContinuous) {
        this.isContinuous = isContinuous;
    }

	@Override
	public long getChangesPending() {
		return changesPending;
	}

	@JsonProperty(required = false, value = "changes_pending")
	public void setChangesPending(long changesPending) {
		this.changesPending = changesPending;
	}

	@Override
    public long getWriteFailures() {
        return writeFailures;
    }

    @JsonProperty(required = false, value = "doc_write_failures")
    public void setWriteFailures(long writeFailures) {
        this.writeFailures = writeFailures;
    }

    @Override
    public long getTotalReads() {
        return totalReads;
    }

    @JsonProperty(required = false, value = "docs_read")
    public void setTotalReads(long totalReads) {
        this.totalReads = totalReads;
    }

    @Override
    public long getTotalWrites() {
        return totalWrites;
    }

    @JsonProperty(required = false, value = "docs_written")
    public void setTotalWrites(long totalWrites) {
        this.totalWrites = totalWrites;
    }

    @Override
    public long getTotalMissingRevisions() {
        return totalMissingRevisions;
    }

    @JsonProperty(required = false, value = "missing_revisions_found")
    public void setTotalMissingRevisions(long totalMissingRevisions) {
        this.totalMissingRevisions = totalMissingRevisions;
    }

    @Override
    public long getTotalRevisionsChecked() {
        return totalRevisionsChecked;
    }

    @JsonProperty(required = false, value = "revisions_checked")
    public void setTotalRevisionsChecked(long totalRevisionsChecked) {
        this.totalRevisionsChecked = totalRevisionsChecked;
    }

    @Override
    public String getSourceDatabaseName() {
        return sourceDatabase;
    }

    @JsonProperty(required = false, value = "source")
    public void setSourceDatabase(String sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
    }

    @Override
    public String getTargetDatabaseName() {
        return targetDatabase;
    }

    @JsonProperty(required = false, value = "target")
    public void setTargetDatabase(String targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    @Override
    public String getSourceSequenceId() {
        return sourceSequenceId;
    }

    @JsonProperty(required = false, value = "source_seq")
    public void setSourceSequenceId(String sourceSequenceId) {
        this.sourceSequenceId = sourceSequenceId;
    }

    @Override
    public String getCheckpointedSourceSequenceId() {
        return checkpointedSourceSequenceId;
    }

    @JsonProperty(required = false, value = "checkpointed_source_seq")
    public void setCheckpointedSourceSequenceId(String checkpointedSourceSequenceId) {
        this.checkpointedSourceSequenceId = checkpointedSourceSequenceId;
    }

	@Override
	public long getCheckpointInterval() {
		return checkpointInterval;
	}

	@JsonProperty(required = false, value = "checkpoint_interval")
	public void setCheckpointInterval(long checkpointInterval) {
		this.checkpointInterval = checkpointInterval;
	}
}
