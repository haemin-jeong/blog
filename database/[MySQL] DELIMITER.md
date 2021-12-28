# MySQL DELIMITER

일반적인 프로그래밍 언어에서와 마찬가지로 MySQL에서도 세미콜론(;)을 사용하여 쿼리문을 구분하는데, DELIMITER 문을 사용하여 구분 문자를 세미콜론이 아닌 다른 문자로 변경할 수 있다.

```sql
DELIMITER 사용할_구분_문자
```

이러한 DELIMITER는 스토어드 프로시저의 범위를 구분하기 위해 사용된다. CREATE PROCEDURE 안에서도 마찬가지로 구분 문자로 세미콜론(;)이 사용되는데 이렇게 되면 어디까지가 스토어드 프로시저의 범위인지 구분하기 어려워진다. 그래서 프로시어의 범위를 구분하기 위해 다음과 같이 DELIMITER를 사용하여 일시적으로 구분 문자를 변경한다.

```sql
DROP PROCEDURE IF EXISTS findMemberByName;
DELIMITER $$ -- 구분 문자를 $$로 변경
CREATE PROCEDURE findMemberByName(IN memberName VARCHAR(10))
BEGIN
    SELECT * FROM umember WHERE name = memberName;
END $$
DELIMITER ; -- 구분 문자를 다시 ;로 변경

CALL findMemberByName('김김김');
```