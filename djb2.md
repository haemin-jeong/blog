# djb2 해시 함수
djb2는 문자열 해시 함수중 간단하면서 무작위 분포를 만드는데 뛰어나다고 알려져 있다.

```java
int djb2(char[] arr) {
    int hash = 5381;

    for (int i = 0; i < arr.length; i++) {
        hash = (((hash << 5) + hash) + arr[i]);
    }

    if(hash < 0) hash *= -1;

    return (hash % HASH_TABLE_SIZE);
}
```
