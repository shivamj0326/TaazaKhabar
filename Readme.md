
# TaazaKhabar

A Simple SpringBoot App which provides top stories and comments from the HackerNews API


## API Reference

#### Get Top Stories
Returns the top stories based on the story score, sorted in decreasing order

```http
  GET /top-stories
```



#### Get Past Stories
Returns the Top Stories which were served by the previous endpoint

```http
  GET /past-stories
```
#### Get Top Comments 
Returns the Top Comments for a given story sorted by the comment thread size in decreasing order

```http
  GET /comments?id
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id`      | `long` | **Required**. Id of story to fetch |



## Caching

Using Hazelcast as an embedded-distributed  cache.

## Asynchronous Execution
Using CompletableFuture with Stream Api for executing tasks


## Configuration
**Limit** : Number of Top Stories and Comments to be returned is by default limited to **10**.
To override the default behaviour, below is the property inside the application.yml

```
taazakhabar.client.hackernews.item.limit
```
**Time to Live** in Cache : 15 (mins) 
Responses are cached for the time provided as part of this configuration.
This is also configurable using the below property.
Note: Please provide the value in **minutes**
```
taazakhabar.client.hackernews.cache.ttl
```
**Thread Configuration** :
Flexibility in terms of creating a ThreadPool. When this property is provided, a FixedThreadPool is created, Otherwise, a CachedThreadPool is created by default.
Using this property, we can control the number of threads to be spawned in order to execute the async tasks.
```
taazakhabar.client.hackernews.threads.max-thread
```

## JUnit Testing

Standard Unit Tests using Mockito Framework are written.

Rest calls are simulated using **Hoverfly**

Hoverfly runs as a proxy web server when the JUnit Tests are running. The behaviour of the server can be simulated using the configuration files in the below folder.

 ```src/test/resources/hoverfly```




## Architecture

![App Screenshot](https://snipboard.io/MV74X2.jpg)

Nginx : Serves as a Reverse Proxy, is also the entry point for our app running on port 8080

App1 & App2 : Two instances of the TaazaKhabar App running on ports 8081 

DB : Postgres Running on 5432

Cache is shared between the two app instances and is synced automatically.
If one of the app instances is down, the cached data is not lost.

With adherence, to this architecture, the docker configurations are created.


## Run Locally

Clone the project

```bash
git clone https://github.com/shivamj0326/TaazaKhabar.git
```

Go to the project directory

```bash
  mvn clean install
```
Start the server

```bash
  mvn spring-boot:run
```


## Deployment

To run the app in a docker container, check into the project directory and do clean package build of the app

```bash
  mvn clean package
```
Run the Docker compose command
```bash
  docker-compose up
```
Application should be running on port 8080

## References

 - [Hoverfly](https://docs.hoverfly.io/en/latest/)
 - [Hazelcast SpringBoot Integration](https://docs.hazelcast.com/tutorials/caching-springboot)


