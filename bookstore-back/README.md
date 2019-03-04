# javaee-getting-started
# to consume Rest 
Client client = ClientBuilder.newClient();
WebTarget target = client.target("http://www.bookstore.com/books/123");
Invocation invocation = target.request(MediaType.APPLICATION_JSON).buildGet();
Response response = invocation.invok();