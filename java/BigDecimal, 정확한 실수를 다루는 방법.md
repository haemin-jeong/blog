# \[Java\] BigDecimal, 정확한 실수를 다루는 방법

최근 개인 프로젝트를 진행하면서 오차가 없어야 하는 실수를 다뤄야 할 일이 있었는데, 무심코 해당 데이터들을 double 타입으로 선언하다가 'double 타입은 오차가 생길 수 있다'라는 문장이 머릿속에 스쳐지나 갔습니다. 이를 계기로 BigDecimal 에 대해 찾아보게 되었고, 기본적인 사용법에 대해 정리하였습니다.

## BigDecimal이란?

자바에서 실수 타입으로 보통 double형을 많이 사용합니다. 그런데 double형은 정밀도가 최대 13자리이고, 근사치를 저장하기 때문에 오차가 발생하여 크고 정확한 실수가 필요한 경우 예상치 못한 문제가 발생할 수 있습니다.

이런 경우 BigDecimal을 사용하여 정확한 실수 값을 표현할 수 있습니다. BigDecimal 클래스 내부를 살펴보면 다음과 같은 필드로 이루어진 것을 볼 수 있습니다.

```java
public class BigDecimal extends Number implements Comparable<BigDecimal> {
    
    private final BigInteger intVal; // 수를 구성하는 전체 자리수

    private final int scale;  // 전체 소수점 자리수

    private transient int precision; // 정밀도

    // ...
}
```

BigDecimal은 정수를 사용하여 값을 저장하는데 intValue × 10<sup>-scale</sup>와 같은 형식으로 수를 표현합니다.

예를 들어 123.45는 12345x10<sup>-2</sup> 로 표현됩니다.

## BigDecimal 생성
BigDecimal객체는 다음과 같이 생성할 수 있습니다.
```java
BigDecinmal value = new BigDecimal("123.45");

//static factory mathod
BigDecinmal value = BigDecimal.valueOf(123.45);
```
valueOf() 메서드의 경우 내부에서 Double.toString() 을 통해 수를 문자열로 변환하여 BigDecimal 객체를 생성하기 떄문에 문자열 인자를 받는 생성자를 통해 생성하는 것과 결과적으로 같다고 볼 수 있고, 공식 문서에 따르면 일반적으로 선호되는 double 또는 float 타입 변수를 BigDecimal로 변환하는 방법이라고 합니다.
```java
public static BigDecimal valueOf(double val) {
    return new BigDecimal(Double.toString(val));
}
```

이 외에도 다양한 생성자를 제공해 주는데, 주의할 점은 double타입 값을 인자로 전달하는 생성자는 근사치 값이 그대로 전달되어 정확한 값이 저장되지 않을 수 있기 때문에 사용하지 않는 것이 좋습니다.

```java
BigDecinmal value = new BigDecimal(123.45); //사용하지 말자!
```

0, 1, 10의 경우는 상수로도 제공을 해줍니다.

```java
BigDecimal zero = BigDecimal.ZERO;
BigDecimal one = BigDecimal.ONE;
BigDecimal ten = BigDecimal.TEN;
```

## BigDecimal의 대소 비교

BigDecimal은 double과 같은 프리미티브 타입이 아닌 객체이기 때문에 값을 비교할 뗴 == 을 사용하면 안됩니다.

```java
BigDecimal bigDecimal1 = new BigDecimal("3.000");
BigDecimal bigDecimal2 = new BigDecimal("3.000");

bigDecimal1 == bigDecimal2 //false
```

다음과 같이 equals() 메서드를 사용하여 == 비교를 할 수 있습니다.

```java
bigDecimal1.equals(bigDecimal2); //true
```

대소를 비교하는 방법은 compareTo() 메서드를 사용하는 것 입니다.

```java
//BigDecimal1이 더 크면 1, 같으면0, 작으면 -1 반환
System.out.println(bigDecimal1.compareTo(bigDecimal2));
```

## BigDecimal 사친역산
BigDecimal의 사칙연산은 다음과 같이 메서드를 사용해서 할 수 있습니다.

```java
BigDecimal bigDecimal1 = new BigDecimal("1.000");
BigDecimal bigDecimal2 = new BigDecimal("3.000");

//덧셈
bigDecimal1.add(bigDecimal2);

//뺄샘
bigDecimal1.subtract(bigDecimal2);

//나눗셈
bigDecimal1.divide(bigDecimal2);

//곱셈
bigDecimal1.multiply(bigDecimal2);
```

## BigDecimal 소수점 처리

그런데 위 예제를 실행하는 경우 나눗셈 부분에서 다음과 같은 에러가 발생하는 것을 볼 수 있습니다.

```
java.lang.ArithmeticException: Non-terminating decimal expansion; no exact representable decimal result.
```

1.0/3.0의 연산 결과가 0.333333.. 으로 무한이기 때문에 발생하는 에러입니다.

이런 경우 다음과 같이 소수점을 어떻게 처리할지 지정할 수 있습니다.

```java
/*
반올림하여 소수점 3번째 자리까지 값을 표현.
divide(BigDecimal divisor, int scale, RoundingMode roundingMode)
*/
bigDecimal1.divide(bigDecimal, 3, RoundingMode.HALF_UP); //0.333
```

반올림 처리 방법은 RoundingMode 에서 제공하는 상수를 사용하면 됩니다.

- RoudingMode.CEILING : 올림
- RoudingMode.FLOOR : 내림
- RoudingMode.UP : 양수일 때는 올림, 음수일 때는 내림
- RoudingMode.DOWN : 양수일 때는 내림, 음수일 때는 올림
- RoudingMode.HALF_UP : 반올림(5이상 올림, 6미만 버림)
- RoudingMode.HALF_DOWN : 반올림(6이상 올림, 6미만 버림)
- RoudingMode.HALF_EVEN : 반올림(반올림 자리의 값이 짝수면 HALF_DOWN, 홀수면 HALF_UP)
- RoudingMode.UNNECESSARY : 나눗셈의 결과가 딱 떨어지는 수가 아니면 ArithmeticException 발생

## 소수점 자리수 변경하기

10을 곱하거나, 나눗셈 하는 대신 scale 값을 변경함으로써 같은 효과를 낼 수 있습니다

```java
BigDecimal	setScale​(int newScale);
BigDecimal	setScale​(int newScale, RoundingMode roundingMode);
```
scale 값을 줄이는 경우 10의 n제곱으로 나누는 것과 같기 때문에 앞선 나눗셈 연산에서와 마찬가지로 반올림 모드를 지정해 주어야 합니다.

## 참고

- 자바의 정석 3판