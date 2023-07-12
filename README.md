# 🎤 코노팟

[APK](https://drive.google.com/file/d/1L8XsUzTsK3z9Dnn7XE95RTjSZIxI1ZPa/view?usp=sharing)

## 팀원

- 강은비
- 유석원

## 프로젝트 및 기능 소개

같이 코노 갈 사람~?? 코인 노래방 팟을 구할 수 있는 Android Application 입니다.

### 구글 소셜 로그인

|구글 소셜 로그인 |회원 정보 입력|
|:---:|:---:|
| <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/cf9eb0c7-282b-4f24-a3f6-ce67b18c5507" width="300" style="object-fit:cover;" /> | <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/a3456c8a-3fa9-4e83-b58a-0c3f36522f91" width="300" style="object-fit:cover;" />|
- 구글 SDK를 이용해 소셜 로그인을 구현했습니다.
- 최초 로그인 시 성별 및 노래 장르를 추가 입력해야 합니다.
- 기본 닉네임은 구글에서 가져온 사용자 이름이고, 수정도 가능합니다.

### 지도 탭

|내 주변 노래방 및 상세 정보|같이 갈 사람 모집글 올리기| 모집글 조회 |
|--|--|--|
| <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/e3a7c4c7-92e9-49bf-ad6c-f77b7a522caf" width="300" style="object-fit:cover;"  /> | <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/f928e551-7ce5-4411-8ea9-2a6fb9f89aea" width="300" style="object-fit:cover;" /> | <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/5fd18465-c967-45c1-87b1-f0a44077d0de" width="300" style="object-fit:cover;" /> |

- 현위치 주변 1km 반경 내에 위치한 코인 노래방들을 볼 수 있습니다.
  - Kakao Map API와 Kakao Local REST API를 이용했습니다.
- 마커를 클릭하면 코인 노래방 상세 정보를 볼 수 있습니다.
- 스와이프하며 코노팟을 구하는 글들을 볼 수 있습니다.
- 같이 갈 사람 찾기 버튼을 눌러 글 정보를 입력하여 올릴 수 있습니다.
- 참여하기 버튼을 눌러 팟에 참여할 수 있습니다.
- 채팅하기 버튼을 눌러 글 올린 사람과 채팅할 수 있습니다.

### 채팅 탭
|채팅방|
|---|
|<img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/742386c7-74b0-486f-959e-4c7030feb1eb" width="300" style="object-fit:cover;" /> <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/c8e23e67-7b30-45c9-8e0d-6ee5945451c4" width="300" style="object-fit:cover;" />|

|채팅 목록|
|--|
| <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/4151c05f-b8bb-465d-b686-81be18fc7483" width="300" style="object-fit:cover;" /> <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/58839bb5-0119-4cc8-93f4-8ce7fc9bf630" width="300" style="object-fit:cover;" />|

- 채팅 기능을 통해 코노팟 유저들과 소통할 수 있습니다.

### 마이페이지 탭

<img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/5064310c-5f49-4559-94e0-45967cad852e" width="300" style="object-fit:cover;"  />

- 내 프로필을 볼 수 있습니다.
- 내가 모집한 글과 참여한 글 목록을 볼 수 있습니다.

## 백로그

- 참여 수락/거절 기능: 참여하기 버튼을 누르면 글 올린 사람이 수락 및 거절할 수 있는 기능
- 채팅에서 읽지 않은 메시지 개수 구하고 표시하는 기능
- 채팅 메시지가 적을 때 화면이 올라가는 것 동적으로 처리
- 내 프로필 수정 기능

