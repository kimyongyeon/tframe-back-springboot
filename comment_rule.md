# 공통 규칙
## Typedoc 표준 
```
/** */ 주석코드 사용, 각 세부 항목에는 ‘@’ 태그 붙여 작성
```
### Single-Line 주석 :
``` 
/** @file abcd.js */
```
### Multi-Line 주석 :
``` 
/**
 * @file abcd.js
 * @author hong gil dong
 * @description This file is…
 */
```
# 주석 종류
- 파일 주석(클래스가 없을 경우) 클래스 주석(클래스가 있을 경우)
```
/**
 * 파일_설명
 * @file 파일명
 * @author 작성자명
 */
```
> 파일 주석은 파일 최상단에, 클래스 주석은 클래스 선언 바로 위에 작성
- 변수 주석 (각 변수 선언 위에)
```
/**
 * 변수 설명
 */
```
- 함수/메서드 주석 (각 함수/메서드 선언 위에)
```
/**
 * @description 함수/메서드_기능_설명
 * @params {타입} 변수명 변수_설명
 * @returns {타입} 리턴_설명
 */
```
