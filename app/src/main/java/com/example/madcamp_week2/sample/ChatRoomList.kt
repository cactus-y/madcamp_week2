package com.example.madcamp_week2.sample

var chatRoom1 =  SampleChatRoom(user2, arrayListOf(
        SampleChatMessage(user1, "Hello", "2023/07/08 16:33:20"),
        SampleChatMessage(user2, "Hello1", "2023/07/08 16:33:21"),
        SampleChatMessage(user2, "Hello2", "2023/07/08 16:33:24"),
        SampleChatMessage(user1, "Hello3", "2023/07/08 16:33:26"),
        SampleChatMessage(user1, "Hello4", "2023/07/08 16:34:20"),
        SampleChatMessage(user2, "Hello5", "2023/07/08 16:36:20"),
        SampleChatMessage(user1, "Hello6", "2023/07/08 16:36:41"),
        SampleChatMessage(user2, "Hello7", "2023/07/08 18:33:22"),
        SampleChatMessage(user1, "Hello8", "2023/07/09 08:12:20"),
        SampleChatMessage(user1, "Hello9", "2023/07/09 10:41:20"),
        SampleChatMessage(user1, "Hello10", "2023/07/09 10:41:20"),
        SampleChatMessage(user1, "Hello11", "2023/07/09 10:41:20"),
        SampleChatMessage(user1, "Hello12", "2023/07/09 10:41:20"),
        SampleChatMessage(user1, "Hello13", "2023/07/09 10:41:20"),
        SampleChatMessage(user1, "Hello14", "2023/07/09 10:41:20"),
        SampleChatMessage(user1, "Hello15", "2023/07/09 10:41:20"),

    )
)

var chatRoom2 =  SampleChatRoom(user4, arrayListOf(
        SampleChatMessage(user4, "Hello", "2023/07/10, 16:33:20"),
        SampleChatMessage(user1, "Hello1", "2023/07/10, 16:33:21"),
        SampleChatMessage(user1, "Hello2", "2023/07/10, 16:33:24"),
        SampleChatMessage(user4, "Hello3", "2023/07/10, 16:33:26"),
        SampleChatMessage(user4, "Hello4", "2023/07/10, 16:34:20"),
        SampleChatMessage(user1, "Hello5", "2023/07/10, 16:36:20"),
        SampleChatMessage(user4, "Hello6", "2023/07/10, 16:36:41"),
        SampleChatMessage(user1, "Hello7", "2023/07/10, 18:33:22"),
        SampleChatMessage(user4, "Hello8", "2023/07/11, 08:12:20"),
        SampleChatMessage(user4, "Hello999999", "2023/07/11, 10:41:20"),
    )
)

var globalChatRoomList = arrayListOf(chatRoom1, chatRoom2)