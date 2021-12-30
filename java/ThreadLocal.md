# ThreadLocal

여러 쓰레드가 같은 인스턴스 필드의 값을 변경하는 상황에 발생하는 동시성 문제를 해결하는 방법으로 자바는 언어 차원에서 ThreadLocal이라는 것을 제공한다.

ThreadLocal은 각 쓰레드 별로 전용 내부 저장소를 만들어 값을 관리하는 방법으로 동시성 문제를 해결한다.

## ThreadLocal 사용법
```java
ThreadLocal<String> threadLocal = new ThreadLocal<>();

//데이터 저장
threadLocal.set("Hello");

//데이터 조회
threadLocal.get();

//데이터 삭제(쓰레드 로컬의 모든 값을 삭제하는 것이 아닌 해당 쓰레드의 값만 삭제)
threadLocal.remove();
```
set을 하면 해당 쓰레드 전용 저장소에 값이 저장되고, get을 하면 전용 저장소에서 값을 조회한다.

해당 쓰레드에서 ThreadLocal을 사용한 후에는 꼭 remove 를 해주는 것이 좋다. WAS(톰켓)처럼 쓰레드 풀을 사용하는 경우 사용 후 값을 제거해 주지 않으면 해당 쓰레드를 사용하게 될 다음 사용자에게 저장했던 값이 그대로 유출되는 상황이 발생한다.

간단한 예제를 통해 동시성 문제가 발생하는 상황과 이를 ThreadLocal을 사용하여 해결해 보자.

## 동시성 문제 발생 예
예를 들어 아래 코드를 보면 thread1과 thread2가 같은 MySotre 인스턴스의 value 필드에 값을 설정하는 것을 볼 수 있다.

thread1은 "안녕"이라는 값을 value로 설정하고 잠시 뒤 value 값을 출력했다. thread1 입장에선 당연히 "안녕"이 출력 될 것이라 생각했겠지만, "Hello"가 출력되었다.

그 이유는 thread1이 value를 설정하고 잠시 기다리는 동안 thread2가 실행되어 value 값을 "Hello"로 바꾸어 버렸기 때문이다.
```java
class MyStore {
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

public class Main {

    public static void main(String[] args) {
        MyStore store = new MyStore();

        Runnable r1 = () -> {
            store.setValue("안녕");

            // 동시성 문제를 발생시키기 위해 딜레이를 주었다.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //"안녕"이 출력되길 기대했지만, "Hello"가 출력
            System.out.println("value = " + store.getValue());
        };

        Runnable r2 = () -> store.setValue("Hello");

        Thread thread1 = new Thread(r1);
        Thread thread2 = new Thread(r2);

        thread1.start();
        thread2.start();
    }
}
```

## ThreadLocal을 사용하여 동시성 문제 해결
```java
class MyStore {
    //value 필드를 ThreadLocal로 변경
    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public String getValue() {
        return threadLocal.get();
    }

    public void setValue(String value) {
        threadLocal.set(value);
    }

    public void remove() {
        threadLocal.remove();
    }
}

public class Main {

    public static void main(String[] args) {
        MyStore store = new MyStore();

        Runnable r1 = () -> {
            store.setValue("안녕");

            // 동시성 문제를 발생시키기 위해 딜레이를 주었다.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //중간에 thread2가 "Hello"를 저장했지만 쓰레드마다 별도의 저장소에 저장되기 때문에 정상적으로 "안녕" 출력
            System.out.println("value = " + store.getValue());

            store.remove()
        };

        Runnable r2 = () -> {} store.setValue("Hello");

        Thread thread1 = new Thread(r1);
        Thread thread2 = new Thread(r2);

        thread1.start();
        thread2.start();
    }
}
```
