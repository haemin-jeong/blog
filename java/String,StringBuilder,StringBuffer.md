## String
String 타입은 자바에서 제공하는 문자열 타입으로 Immutable(불변) 하다는 특성을 가지고 있습니다.

이러한 특성 때문에 String 타입 문자열은 덧셈과 같은 연산이 일어나게 되면 매 연산마다 새로운 객체가 생성되게 됩니다.

예를 들어 다음과 같은 코드를 실행한다고 해봅시다.
```java
public class App {
    public static void main(String[] args) {
        String a = "Hello ";
        a += "World";
        a += "!!";
        System.out.println(a); //출력 : Hello World!!
    }
}
```
얼핏 보면 단순히 "Hello " 문자열에 "World", "!!" 문자열이 더해져서 출력되는 것 같지만 ConstantPool을 살펴보면 아래 그림과 같이 "Hello World", "Hello World!!" 라는 문자열이 추가적으로 새로 생성된 것을 볼 수 있고, String 문자열을 연산하는 것은 비효율적인 것을 알 수 있습니다. 

![img](/images/string.png)

## StringBuffer
앞서 살펴본 비효율적인 문자열 연산은 StringBuffer를 사용해서 해결할 수 있습니다. StringBuffer는 내부에 버퍼를 가지고 있으며 가변 문자열을 만들 때 주로 사용합니다.

StringBuffer는 생성자를 통해 초기 문자열 또는 버퍼 사이즈를 지정할 수 있으며 버퍼 사이즈를 지정하지 않으면 기본적으로 16으로 생성되고, 버퍼 사이즈가 꽉 찰 경우 자동으로 버퍼 사이즈가 늘어나게 됩니다.

StringBuffer의 생성자
- `StringBuffer()` : 초기 문자열이 없고, 버퍼 사이즈를 16인 객체를 생성한다.
- `StringBuffer(int capacity)` : 초기 문자열이 없고, 버퍼 사이즈가 capacity인 객체를 생성한다.
- `StringBuffer(String str)`, `StringBuffer(CharSequence seq)` : 지정된 문자열로 초기화 된 객체를 생성한다.

append 메서드를 통해 문자열을 추가할 수 있으며, 이 외에도 [StringBuffer 공식 문서](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StringBuffer.html)를 살펴보면 여러가지 편의 메서드를 제공하는 것을 볼 수 있습니다.

```java
public class App {
    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        sb.append("Hello ");
        sb.append("World");
        sb.append("!!");

        System.out.println(sb); //출력 : Hello World!!
    }
}
```
### StringBuilder
StringBuffer와 같은 기능을 하며 사용법도 같은 StringBuilder라는 클래스가 있습니다. 그렇다면 이 둘의 차이는 무엇일까요?

```java
public class App {
    public static void main(String[] args) {
        //StringBuffer와 사용법 동일!
        StringBuilder sb = new StringBuilder();
        sb.append("Hello ");
        sb.append("World");
        sb.append("!!");

        System.out.println(sb); //출력 : Hello World!!
    }
}
```

StringBuffer와 StringBuilder 의 차이점은 **동기화의 유무**입니다. StringBuffer는 동기화를 지원하여 멀티 쓰레드 환경에서 안전하게 사용할 수 있고, StringBuilder 는 동기화를 지원하지 않은 만큼 StringBuffer 보다 좀 더 좋은 성능을 제공합니다.

따라서 동기화를 고려하지 않아도 되는 상황이라면 StringBuilder를 사용하는 것이 더 효율적입니다.

## 정리
StringBuffer는 가변 문자열을 만들때 String보다 효율적인 반면에 String에 비해 메모리 사용량도 많고, 속도도 더 느리기 때문에 상황에 맞게 사용하는 것이 좋습니다.

 즉, 문자열 변경 작업이 많이 일어나고 동기화가 필요하다면 StringBuffer, 동기화가 필요하지 않다면 StringBuilder를 사용하고, 문자열 변경 작업이 일어나지 않는다면 String을 사용하는 것이 좋습니다.

## 참고
- https://wikidocs.net/276
- https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StringBuffer.html
