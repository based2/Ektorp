package org.ektorp.impl;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.ektorp.ActiveTask;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
   use = JsonTypeInfo.Id.NAME,
   include = JsonTypeInfo.As.PROPERTY,
   property = "type")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonSubTypes({
   @Type(value = StdReplicationTask.class, name = "replication"),
   @Type(value = StdIndexerTask.class, name = "indexer"),
   @Type(value = StdDatabaseCompactionTask.class, name = "database_compaction"),
   @Type(value = StdViewCompactionTask.class, name = "view_compaction") })
public abstract class StdActiveTask implements ActiveTask {

    private String pid;
    private int progress;
    private Date startedOn;
    private Date updatedOn;
	private String node;

	@Override
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public Date getStartedOn() {
        return startedOn;
    }

    @JsonProperty(value = "started_on")
    public void setStartedOn(Date startedOn) {
        this.startedOn = startedOn;
    }

    @Override
    public Date getUpdatedOn() {
        return updatedOn;
    }

    @JsonProperty(value = "updated_on")
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

	public String getNode() {
		return node;
	}

	@JsonProperty(required = false, value = "node")
	public void setNode(String node) {
		this.node = node;
	}
}
