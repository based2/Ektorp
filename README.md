# Ektorp [![build status](https://secure.travis-ci.org/helun/Ektorp.png)](http://travis-ci.org/helun/Ektorp) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.ektorp/org.ektorp/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.ektorp/org.ektorp)

Ektorp is a persistence API that uses [CouchDB](http://couchdb.apache.org/) as storage engine. The goal of Ektorp is to combine JPA like functionality with the simplicity and flexibility that CouchDB provides.

Features
--------
Here are some good reasons why you should consider using Ektorp in your project:

* *Rich domain models.* With the powerful JSON-object mapping provided by [Jackson][jackson] (2.6!) it is easy to create rich domain models.
* *Schemaless comfort.* As CouchDB is schemaless, the database gets out of the way during application development. With a schemaless database, most adjustments to the database become transparent and automatic.
* *Out-of-the-Box CRUD.* The generic repository support makes it trivial to create persistence classes.
* *Simple and fluent API.*
* *Spring Support.* Ektorp features an optional spring support module. (3.2!)
* *Choice of abstraction level.* From full object-document mapping to raw streams, Ektorp will never stop you if you need to step down an abstraction level.

Documentation
-------------
[API-Reference](https://helun.github.io/Ektorp/reference_documentation.html)

Simple API
----------
It is very easy to get started with Ektorp:

```java
HttpClient httpClient = new StdHttpClient.Builder()
        .url("http://localhost:5984")
        .build();

CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
CouchDbConnector db = new StdCouchDbConnector("mydatabase", dbInstance);

db.createDatabaseIfNotExists();

Sofa sofa = db.get(Sofa.class, "ektorp");
sofa.setColor("blue");
db.update(sofa);
```

Out-of-the-Box CRUD
-------------------
Ektorp features a generic repository support class. It provides all Create, Read, Update and Delete operations for a persistent class.

Here's how a SofaRepository implemented with the generic repository looks like

```java
public class SofaRepository extends CouchDbRepositorySupport<Sofa> {

    public SofaRepository(CouchDbConnector db) {
        super(Sofa.class, db);
    }

}
```

This repository will have the following methods "out of the box":

```java
SofaRepository repo = new SofaRepository(db);

repo.add(Sofa s);
repo.contains("doc_id");
Sofa sofa = repo.get("doc_id");
repo.update(Sofa s);
repo.remove(Sofa s);
List<Sofa> repo.getAll();
```

Convenient Management of View Definitions
---------------------------------------
The concept of views in CouchDB can be a little daunting at first and there will always be the task of managing view definitions to go along your mapped classes.
Ektorp provides two solutions for this:

Embedded View Definitions
-------------------------
It is possible to embed view definitions in your repository classes through a @View annotation:

```java
@View( name="complicated_view", file = "complicated_view.json")
public class BlogPostRepository extends CouchDbRepositorySupport<BlogPost> {

    @Autowired
    public BlogPostRepository(@Qualifier("blogPostDatabase") CouchDbConnector db) {
        super(BlogPost.class, db);
        initStandardDesignDocument();
    }

    @Override
    @View( name="all", map = "function(doc) { if (doc.title) { emit(doc.dateCreated, doc._id) } }")
    public List<BlogPost> getAll() {
        ViewQuery q = createQuery("all").descending(true);
        return db.queryView(q, BlogPost.class);
    }

    @GenerateView
    public List<BlogPost> findByTag(String tag) {
        return queryView("by_tag", tag);
    }

}
```

Automatic view generation for finder methods
--------------------------------------------
Finder methods annotated with @GenerateView will have their view definitions automatically created.
CouchDbRepositorySupport will generate a "by_tag" view in CouchDB at application start up for the method "findByTag" in the example above.

Simple and Powerful JSON / Object Mapping
-----------------------------------------
The JSON / Object mapping in Ektorp is handled by the excellent [Jackson JSON library][jackson].

Jackson makes it easy to map the common cases and provides for instance the possibility to map polymorph types for more advanced use cases.

All persistent objects managed by Ektorp need to define properties for id and revision and they need to be accessible by getters and setters.

Here's an trivial example class:

```java
import org.codehaus.jackson.annotate.*;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({"id", "revision"})
public class Sofa {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("_rev")
    private String revision;

    private String color;

    public void setId(String s) {
        id = s;
    }

    public String getId() {
        return id;
    }

    public String getRevision() {
        return revision;
    }

    public void setColor(String s) {
        color = s;
    }

    public String getColor() {
        return color;
    }
}
```

Querying Views
--------------
There are several methods for querying CouchDB [views](http://wiki.apache.org/couchdb/Introduction_to_CouchDB_views) from Ektorp.

Query for Objects
-----------------
If the view's result value field is a document, Ektorp can load the result as a List of Objects

```java
ViewQuery query = new ViewQuery()
        .designDocId("_design/Sofa")
        .viewName("by_color")
        .key("red");

List<Sofa> redSofas = db.queryView(query, Sofa.class);
```

Scalar queries
--------------
It is possible to query for scalar values. Currently just String and int values are supported.

```java
ViewQuery query = new ViewQuery()
        .designDocId("_design/somedoc")
        .viewName("some_view_name");

ViewResult result = db.queryView(query);
for (ViewResult.Row row : result.getRows()) {
    String stringValue = row.getValue();
    int intValue = row.getValueAsInt();
}
```

It is of course possible to parse a string value as JSON.
View Result as Raw JSON Stream
------------------------------
The most flexible method is query for stream. The result is returned as a stream.

```java
ViewQuery query = new ViewQuery()
        .designDocId("_design/somedoc")
        .viewName("view_with_huge_result");

InputStream data = db.queryForStream(query);
// ...
data.close();
```

Try it Out
------------
### Download binaries from [maven repository](http://central.maven.org/maven2/org/ektorp/org.ektorp/)

If you are using Maven:

```xml
<dependency>
    <groupId>org.ektorp</groupId>
    <artifactId>org.ektorp</artifactId>
    <version>1.5.0</version>
</dependency>
```

Getting Help
------------
You can usually get quick answers at the [Ektorp google group](http://groups.google.com/group/ektorp-discuss)

[jackson]: http://wiki.fasterxml.com/JacksonHome

=====
One or more dependencies were identified with known vulnerabilities in Ektorp:

jackson-databind-2.6.7.jar (com.fasterxml.jackson.core:jackson-databind:2.6.7, cpe:/a:fasterxml:jackson-databind:2.6.7, cpe:/a:fasterxml:jackson:2.6.7) : CVE-2017-15095, CVE-2017-17485, CVE-2017-7525, CVE-2018-7489, CVE-2018-5968

spring-core-3.2.18.RELEASE.jar (cpe:/a:pivotal_software:spring_framework:3.2.18, cpe:/a:springsource:spring_framework:3.2.18, org.springframework:spring-core:3.2.18.RELEASE, cpe:/a:pivotal:spring_framework:3.2.18) : CVE-2018-1258, CVE-2018-1271, CVE-2018-1270, CVE-2016-5007, CVE-2018-1272