일급 컬렉션이라는 것을 최근 처음 알게 되어 정리를 하면서도 아직 잘 와닿지 않는 부분들도 있는것 같습니다. 일급 컬렉션이 객체지향적으로, 리팩토링하기 좋은 코드를 작성 하는 데 도움이 된다고 하는 만큼 앞으로 프로젝트에 적용해 보며 익숙해져야겠다는 생각이 들었습니다.

## 일급 컬렉션이란?

일급 컬렉션이란 컬렉션을 Wrapping하면서 컬렉션 외에 다른 멤버 변수가 없는 클래스를 말합니다.

예를 들어 다음과 같이 Study 클래스가 있고, 멤버 변수 중 하나로 스터디원 member에 대한 리스트를 가지고 있다고 해보겠습니다.

```java
public class Study {

    private String name;
    List<Member> members;

    // ...
}

class Person {
    String name;
    String sex;
    int age;
    
    // ...
}
```
여기서 members 리스트를 일급 컬렉션으로 만들어 보겠습니다. 방법은 앞서 설명했듯이 일급 컬렉션으로 만들려면 members 리스트를 Wrapping 하는 클래스를 만들고, 해당 클래스는 members 리스트 외에 다른 멤버 변수를 가지고 있지 않으면 됩니다.

```java
public class Study {

    private String name;
    private boolean isFree;
    
    public Study(String name, List<Member> members) {
        this.name = name;
        this.members = members;
    }
}

//일급 컬렉션
class Members {
    private List<Member> members;

    public Members(List<Member> members) {
        this.members = members;
    }
}

class Member {
    String name;
    String sex;
    int age;
}
```

## 그렇다면 굳이 이렇게 컬렉션을 Wrapping해서 사용하는 이유는 무엇일까요?

### 1. 비즈니스 종속적인 코드를 작성할 수 있다.

예를 들어 한 스터디당 스티디원의 최대 인원은 10명이라는 조건이 추가되었다고 해보겠습니다. 먼저 일급 컬렉션을 사용하지 않고 코드를 작성해 보겠습니다.

```java
public class Study {

    private String name;
    private boolean isFree;
    private List<Member> members;

    public Study(String name, boolean isFree, List<Member> members) {
        validate(members);
        this.name = name;
        this.isFree = isFree;
        this.members = members;
    }

    private void validate(List<Member> members) {
        if (members.size() > 10) {
            throw new IllegalArgumentException("멤버는 최대 10명입니다.");
        }
    }
}
```
스터디 클래스가 members 검증에 대한 역할까지 맡게 되고, 조금 억지스러운 상황이긴 하지만 스터디가 아닌 동아리 클래스에서도 members 를 사용한다면 동아리 클래스서도 동일한 검증 로직을 추가해 주어야 하는 번거로움이 생기며, 협업을 하는 상황이라면 다른 개발자가 코드만 보고 members를 생성하면서 검증을 해줘야 하는지 바로 알 수 없을 것 입니다.

아래와 같이 일급 컬렉션을 사용하여 검증 로직을 Members에서 처리한다면 더이상 Members 외부에서 검증에 대한 걱정을 하지 않아도 되고, 중복 코드도 줄일 수 있게 됩니다. 

```java
public class Study {

    private String name;
    private boolean isFree;
    private Members members;
    
    //더이상 Members 검증에 대한 걱정을 외부에서 하지 않아도 된다.
    public Study(String name, boolean isFree, Members members) {
        this.name = name;
        this.isFree = isFree;
        this.members = members;
    }
}

//일급 컬렉션
class Members {
    private List<Member> members;

    public Members(List<Member> members) {
        validate(members);
        this.members = members;
    }

    private void validate(List<Member> members) {
        if (members.size() > 10) {
            throw new IllegalArgumentException("스터디원은 최대 10명입니다.");
        }
    }
}
```

### 2. 상태와 행위를 한 곳에서 관리할 수 있다. - 캡슐화?

스터디 멤버 중에서 성인인 멤버만 조회하는 기능을 추가한다고 가정해 보겠습니다. 일급 컬렉션을 사용하지 않고 해당 기능을 추가한다면 다음과 같이 작성할 수 있을 것 입니다.

```java
public class Study {

    private String name;
    private boolean isFree;
    private List<Member> members;

    // ...

    public List<Member> findAdults() {
        return members.stream()
                .filter(m -> m.getAge() >= 20)
                .collect(Collectors.toList());
    }
}
```

코드를 보면 스터디 클래스에 대한 역할이 많아지는 것 같고, 스터디 뿐만 아니라 members를 동아리 등 다른 클래스에서도 사용한다면 클래스가 늘어나는 만큼 중복 코드도 늘어날 것입니다.  

```java
public class Study {
    private List<Member> members;

    // ...

    public List<Member> findAdults() {
        return members.stream() // 중복 코드
                .filter(m -> m.getAge() >= 20)
                .collect(Collectors.toList());
    }
}

public class Club {

    private List<Member> members;

    // ...

    
    public List<Member> findAdults() {
        return members.stream() // 중복 코드
                .filter(m -> m.getAge() >= 20)
                .collect(Collectors.toList());
    }
}
```

일급 컬렉션을 사용하여 코드를 작성해 보겠습니다.

```java
public class Study {

    private Members members;

    // ...

    public List<Member> findAdults() {
        return members.findAdults();
    }
}

public class Club {

    private Members members;

    // ...

    public List<Member> findAdults() {
        return members.findAdults();
    }
}

//일급 컬렉션
class Members {
    private List<Member> members;

    // ...

    public List<Member> findAdults() {
        return members.stream()
                .filter(m -> m.getAge() >= 20)
                .collect(Collectors.toList());
    }
}
```

스터디, 동아리 클래스가 가지고 있던 members에 대한 역할을 분리하고, Members 클래스로 상태와 행위를 캡슐화 함으로써 클래스의 책임을 덜고, 중복 코드도 줄일 수 있었습니다.

### 3. 컬렉션을 불변으로 만들 수 있다.

자바에서 변수를 불변으로 만드는 방법으로는 final 키워드를 붙이는 것입니다. 하지만 final 키워드는 변수에 다른 값을 재할당 하는 것을 막아주는 것이기 때문에 컬렉션의 데이터를 변경하는 것은 막을 수는 없습니다.

```java
@Test
@DisplayName("final 키워드로는 컬렉션을 불변으로 만들 수 없다.")
public void finalTest() {
    final List<String> list = new ArrayList<>();

    list.add("Hello"); 
    list.remove("World");
}
```

일급 컬렉션을 사용하여 외부에서 컬렉션에 직접 접근할 수 없도록 막고, 메서드를 통해서만 컬렉션의 값을 읽을 수 있게 만들면 컬렉션을 불변으로 만들 수 있습니다.

```java
class Members {
    private List<Member> members;

    public Members(List<Member> members) {
        this.members = members;
    }

    public Member findMember(int index) {
        return members.get(index);
    }
}
```

만약 컬렉션을 통째로 외부로 반환해야 한다면, Collections.unmodifiableList를 사용하여 읽기 전용 불변 리스트로 반환해 줄 수 있습니다.

```java
class Members {
    private List<Member> members;

    public Members(List<Member> members) {
        this.members = members;
    }

    // ...

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }
}
```

잘못된 부분이나 보충되었으면 하는 내용이 있으시다면 언제든지 지적 부탁드립니다:) 

해당 글은 아래 글들을 참고하여 작성하였습니다.
- https://jojoldu.tistory.com/412
- https://tecoble.techcourse.co.kr/post/2020-05-08-First-Class-Collection/

