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
| <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/a117c372-0d84-4d09-99cb-6b932ffe4200" width="200" style="object-fit:cover;" /> <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/62629f42-58c5-4c9a-85dd-1a222dd35f6f" width="200" style="object-fit:cover;"  /> | <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/5804a3e7-9aed-4319-a76c-61e31739b899" width="200" style="object-fit:cover;" /> <img src="https://github.com/cactus-y/madcamp_week2_front/assets/89760088/2512432a-1b45-4432-aa19-c709c12c6093" width="200" style="object-fit:cover;"  /> |
- 구글 SDK를 이용해 소셜 로그인을 구현했습니다.
- 최초 로그인 시 성별 및 노래 장르를 추가 입력해야 합니다.
- 기본 닉네임은 구글에서 가져온 사용자 이름이고, 수정도 가능합니다.

### 지도 탭

- 현위치 주변 1km 반경 내에 위치한 코인 노래방들을 볼 수 있습니다.
  - Kakao Map API와 Kakao Local REST API를 이용했습니다.
- 마커를 클릭하면 코인 노래방 상세 정보를 볼 수 있습니다.
- 스와이프하며 코노팟을 구하는 글들을 볼 수 있습니다.
- 참여하기 버튼을 눌러 팟에 참여할 수 있습니다.
- 채팅하기 버튼을 눌러 글 올린 사람과 채팅할 수 있습니다.

### 채팅 탭

- 채팅 기능을 통해 코노팟 유저들과 소통할 수 있습니다.

### 마이페이지 탭

- 내 프로필을 볼 수 있습니다.
- 내가 모집한 글과 참여한 글 목록을 볼 수 있습니다.

## 백로그

- 참여 수락/거절 기능: 참여하기 버튼을 누르면 글 올린 사람이 수락 및 거절할 수 있는 기능
- 채팅에서 읽지 않은 메시지 개수 구하고 표시하는 기능
- 채팅 메시지가 적을 때 화면이 올라가는 것 동적으로 처리
- 내 프로필 수정 기능

