## 문제
https://programmers.co.kr/learn/courses/30/lessons/17687
## 풀이 코드
정수를 특정 진법으로 변환한 문자열을 반환해 주는 convert()메서드를 사용해서 t번째 튜브의 순서가 포함된 숫자까지 구한 후, 튜브가 말해야 되는 순서의 숫자들을 문자열로 만들어 반환한다.
```java
public class Solution {
    public String solution(int n, int t, int m, int p) {
        StringBuilder sb = new StringBuilder();

        //t번째 튜브의 순서
        int limit = m*(t-1) + p;

        int i = 0;
        //t번째 튜브의 순서가 포함된 숫자까지만 구한다.
        while(sb.length() < limit) { 
            sb.append(convert(i++, n));
        }

        StringBuilder sb2 = new StringBuilder();
        for(i = p; i <= t*m; i += m) {
            sb2.append(sb.charAt(i-1));
        }

        return sb2.toString();
    }

    /**
     정수 num 을 n 진법으로 변환한 문자열을 반환한다.
     */
    public String convert(int num, int n) {
        if(num == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        while(num >= 1) {
            sb.append(num%n >= 10 ? Character.toString(num%n+55) : num%n);
            num /= n;
        }

        return sb.reverse().toString();
    }
}
```