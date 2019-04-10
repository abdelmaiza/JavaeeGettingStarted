# JavaeeGettingStarted
java -jar swagger-codegen-cli.jar generate -i E:/javaeeGettingStarted/bookstore-back/src/main/webapp/swagger.json -l typescript-angular -o E:/javaeeGettingStarted/bookstore-front/src/app/service</br>
to consume Rest
Client client = ClientBuilder.newClient(); WebTarget target = client.target("http://www.bookstore.com/books/123"); Invocation invocation = target.request(MediaType.APPLICATION_JSON).buildGet(); Response response = invocation.invok();
