package org.ektorp.impl.changes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ektorp.StreamingChangesResult;
import org.ektorp.changes.DocumentChange;
import org.ektorp.http.HttpResponse;
import org.ektorp.impl.ResponseOnFileStub;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class StdDocumentChangeTest {

    //private final static Logger LOG = LoggerFactory.getLogger(StdDocumentChangeTest.class);

	private ObjectMapper mapper = new ObjectMapper();

	@Test
	public void test_normal_message() {
	    try {
            StdDocumentChange m = new StdDocumentChange(load("change_message.json"));
            assertMandatoryFields(m);
            assertNull(m.getDoc());
            assertTrue(m.getDocAsNode().isMissingNode());
            assertFalse(m.isDeleted());
        } catch (IOException e) {
	        e.printStackTrace();
	        fail();
        }
	}

	private void assertMandatoryFields(StdDocumentChange m) {
		assertEquals(21, m.getSequence());
		assertEquals("doc_id", m.getId());
		assertEquals("doc_rev", m.getRevision());
	}

	@Test
	public void test_deleted_doc_message() {
	    try {
            StdDocumentChange m = new StdDocumentChange(load("change_message_w_deleted_doc.json"));
            assertMandatoryFields(m);
            assertTrue(m.isDeleted());
        } catch (IOException e) {
            e.printStackTrace();
            fail("");
        }
	}

	@Test
	public void test_message_with_included_doc()  {
        try {
            StdDocumentChange m = new StdDocumentChange(load("change_message_w_included_doc.json"));
            assertMandatoryFields(m);
            assertNotNull(m.getDoc());
            assertFalse(m.getDocAsNode().isMissingNode());
            assertNotNull(m.getDocAsNode().findValue("_id"));
            assertNotNull(m.getDocAsNode().findValue("_rev"));
        } catch (IOException e) {
            e.printStackTrace();
            fail("");
        }
	}

    @Test
    public void getRevision_should_return_the_first_revision_when_there_are_multiple_changes() {
	    try {
            StdDocumentChange objectUnderTest = new StdDocumentChange(load("change_message_w_multiple_revs.json"));
            assertThat(objectUnderTest.getId(), is("doc_id"));
            assertThat(objectUnderTest.getRevision(), is("rev-first"));
            assertNull(objectUnderTest.getDoc());
            assertTrue(objectUnderTest.getDocAsNode().isMissingNode());
            assertFalse(objectUnderTest.isDeleted());
        } catch (IOException e) {
            e.printStackTrace();
            fail("");
        }
    }

    @Test
    public void getRevisions_should_return_a_List_of_all_the_changes() {
        try {
            StdDocumentChange objectUnderTest = new StdDocumentChange(load("change_message_w_multiple_revs.json"));
            assertThat(objectUnderTest.getId(), is("doc_id"));
            assertThat(objectUnderTest.getRevision(), is("rev-first"));
            assertThat(objectUnderTest.getRevisions(), notNullValue());
            List<String> revisions = objectUnderTest.getRevisions();
            org.assertj.core.api.Assertions.assertThat(revisions).containsExactly("rev-first", "rev-second", "rev-third");
            //assertThat(objectUnderTest.getRevisions(), hasItems("rev-first", "rev-second", "rev-third"));
            assertThat(objectUnderTest.getRevisions().size(), is(3));
            assertNull(objectUnderTest.getDoc());
            assertTrue(objectUnderTest.getDocAsNode().isMissingNode());
            assertFalse(objectUnderTest.isDeleted());
        } catch (IOException e) {
            e.printStackTrace();
            fail("");
        }
    }

    @Test
    public void test_streaming_changes() {
	    HttpResponse httpResponse = ResponseOnFileStub.newInstance(200, "changes/changes_full.json");
	    
	    StreamingChangesResult changes = new StreamingChangesResult(new ObjectMapper(), httpResponse);
	    int i = 0;
        for (DocumentChange documentChange : changes) {
            assertEquals(++i, documentChange.getSequence());
        }
        assertEquals(5, changes.getLastSeq());
        changes.close();
    }

	private JsonNode load(final String id) throws IOException {
        try (final InputStream resourceAsStream = getClass().getResourceAsStream(id)){
            return mapper.readTree(resourceAsStream);
        }
	}

}