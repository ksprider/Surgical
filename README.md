# Surgical
A tool for dynamically filtering entity properties based on Jackson.


## Usage

1. Add denpendency
```xml
<dependency>
    <groupId>com.github.ksprider</groupId>
    <artifactId>Surgical</artifactId>
    <version>0.0.5</version>
</dependency>
```
2. Enable Surgical with `@EnableJSON`
```java
@SpringBootApplication
@EnableJSON
public class RestServiceApplication {
    public static void main(String[] args) {
         SpringApplication.run(RestServiceApplication.class, args);
    }
}
```

3. Add annotation to method
```java
@JSON
```


## Demo

> Object which will to be filted must implement java.io.Serializable interface.

```java
class Tiger implements Serializable {
    public int no;
    public String name;
    public int age;

    public Zoo zoo;
}

class Zoo implements Serializable {
    public String name;
    public String address;

    public City city;
}

class City implements Serializable {
    public int id;
    public String name;

}
```

### All properties
```java
@GetMapping("/demo0")
@JSON("no,name,age,zoo(name,address,city(id,name))")
public Tiger demo0() {
    City city = new City();
    city.id = 100001;
    city.name = "Singapore";

    Zoo zoo = new Zoo();
    zoo.name = "Singapore Zoo";
    zoo.address = "80 Mandai Lake Rd, Singapore 729826";
    zoo.city = city;

    Tiger tiger = new Tiger();
    tiger.no = 1;
    tiger.name = "Pasha";
    tiger.age = 4;
    tiger.zoo = zoo;
    return tiger;
}
```

`[{"no":1,"name":"Pasha","age":4,"zoo":{"name":"Singapore Zoo","address":"80 Mandai Lake Rd, Singapore 729826","city":{"id":100001,"name":"Singapore"}}}]`

### Some properties

#### demo1
```java
@GetMapping("/demo1")
@JSON("no,name,zoo(name,city(name))")
public Tiger demo1() {
    // same with above
    return tiger;
}
```
```json
{"no":1,"name":"Pasha","zoo":{"name":"Singapore Zoo","city":{"name":"Singapore"}}}
```


#### demo2
```java
@GetMapping("/demo2")
@JSON("no,name")
public Tiger demo2() {
    // same with above
    return tiger;
}
```
```json
{"no":1,"name":"Pasha"}
```

#### demo3
```java
@GetMapping("/demo3")
@JSON("no,name,zoo(name,city(*))")
public Tiger demo3() {
    // same with above
    return tiger;
}
```
```json
{"no":1,"name":"Pasha","zoo":{"name":"Singapore Zoo","city":{"id":100001,"name":"Singapore"}}}
```
