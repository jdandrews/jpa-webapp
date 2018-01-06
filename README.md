# jpa-webapp
Basic tomcat template: gradle, JPA, JSF, CDI (Weld)

Deployment:
* Tomcat 8.5+
* Java 1.8
** Java 1.9 doesn't contain JAXException out of the box; not sure why, but this fails in that configuration
* MySQL (but any database will work--you need to update configurations to match your DB type, including login info)

TODO:
* authentication and authorization framework
* good UI samples

