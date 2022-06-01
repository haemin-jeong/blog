# ObjectMapper에서 LocalDateTime 타입 미지원 에러 해결하기

```java
class LocalDateTimeDTO {

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime localDateTime;

    public MyDTO(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}

@Test
void objectMapperTest() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.writeValueAsString(LocalDateTime.now());
}
```

위와 같이 아무 설정 없이 ObjectMapper 객체를 생성해서 LocalDateTime 타입의 객체를 직렬화/역직렬화를 시도하면 다음과 같이 자바8의 date/time 타입을 지원하지 않는다는 에러가 발생합니다.

```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type `java.time.LocalDateTime` not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling
```

이를 해결하는 방법은 ObjectMapper 객체를 생성할 때 다음과 같이 JavaTimeModule을 등록해 주는 것 입니다.

먼저 아래 의존성을 추가해줍니다.
https://mvnrepository.com/artifact/com.fasterxml.jackson.datatype/jackson-datatype-jsr310

만약 스프링부트를 사용하고 있다면 해당 의존성을 포함하고 있기 때문에 추가해주지 않아도 됩니다. 

**여기에 추가로 스프링부트를 사용하는 경우 직접 ObjectMapper를 생성해서 사용하지 않고 스프링부트가 등록 해주는 ObjectMapper 빈을 주입받아 사용하면 아무런 추가 작업 없이 LocalDateTime 타입을 직렬화/역직렬화 할 수 있습니다.**

그리고 다음과 같이 ObjectMapper를 생성하면서 JavaTimeModule을 등록 해줍니다.
```java
@Test
    void name() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); //JavaTimeModule 등록
        String s = objectMapper.writeValueAsString(new LocalDateTimeDto(LocalDateTime.now()));
        System.out.println(s);
}
```
직렬화된 결과가 정상적으로 출력되는 것을 볼 수 있습니다.
```json
{
    "localDateTime":"2022-06-02 12:50:42"
}
```
